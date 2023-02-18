/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.example.netty.tcp.client;

import java.util.Objects;
import java.util.Optional;

import org.reactivestreams.Publisher;

import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.tcp.TcpClient;

/**
 * Reactor Netty based TCP client.
 *
 * @author bernardo.martinezg
 *
 */
@Slf4j
public final class ReactorNettyTcpClient implements Client {

    private Connection           connection;

    /**
     * Host for the server to which this client will connect.
     */
    private final String         host;

    private final ClientListener listener;

    /**
     * Port for the server to which this client will connect.
     */
    private final Integer        port;

    private Optional<String>     response = Optional.empty();

    public ReactorNettyTcpClient(final String hst, final Integer prt, final ClientListener lst) {
        super();

        port = Objects.requireNonNull(prt);
        host = Objects.requireNonNull(hst);
        listener = Objects.requireNonNull(lst);
    }

    @Override
    public final void close() {
        listener.onClose();

        connection.onDispose()
            .block();
    }

    @Override
    public final void connect() {

        listener.onConnect();

        connection = TcpClient.create()
            // Sets connection
            .host(host)
            .port(port)
            // Adds request handler
            .handle(this::handleRequest)
            .connectNow();
    }

    @Override
    public final void request(final String message) {
        log.debug("Sending message {}", message);

        connection.outbound()
            .sendString(Mono.just(message))
            .then()
            .doOnNext(next -> {
                listener.onRequest(message, response, true);
            })
            .subscribe(null, null, connection::dispose);
    }

    private final void handleError(final Throwable ex) {
        log.error(ex.getLocalizedMessage(), ex);
    }

    private final Publisher<Void> handleRequest(final NettyInbound request, final NettyOutbound response) {
        return request.receive()
            .doOnNext(next -> {
                this.response = Optional.ofNullable(next.toString(CharsetUtil.UTF_8));
                listener.onRequest("", this.response, true);
            })
            .doOnError(this::handleError)
            .then();
    }

}
