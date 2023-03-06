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
import java.util.function.BiFunction;

import org.reactivestreams.Publisher;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.tcp.TcpClient;

/**
 * Reactor Netty based TCP client.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class ReactorNettyTcpClient implements Client {

    /**
     * Main connection. For sending messages and reacting to responses.
     */
    private Connection                                                     connection;

    /**
     * IO handler for the client.
     */
    private final BiFunction<NettyInbound, NettyOutbound, Publisher<Void>> handler;

    /**
     * Host for the server to which this client will connect.
     */
    private final String                                                   host;

    /**
     * Transaction listener. Reacts to events during the request.
     */
    private final TransactionListener                                      listener;

    /**
     * Port for the server to which this client will connect.
     */
    private final Integer                                                  port;

    /**
     * Wiretap flag.
     */
    @Setter
    @NonNull
    private Boolean                                                        wiretap = false;

    public ReactorNettyTcpClient(final String hst, final Integer prt, final TransactionListener lst) {
        super();

        port = Objects.requireNonNull(prt);
        host = Objects.requireNonNull(hst);
        listener = Objects.requireNonNull(lst);

        handler = new InboundToListenerIoHandler(listener);
    }

    @Override
    public final void close() {
        log.trace("Stopping client");

        listener.onStop();

        connection.dispose();

        log.trace("Stopped client");
    }

    @Override
    public final void connect() {
        log.trace("Starting client");

        log.debug("Connecting to {}:{}", host, port);

        listener.onStart();

        connection = TcpClient.create()
            // Wiretap
            .wiretap(wiretap)
            // Sets connection
            .host(host)
            .port(port)
            // Adds handler
            .handle(handler)
            // Connect
            .connectNow();

        log.trace("Started client");
    }

    @Override
    public final void request() {
        final Publisher<String> dataStream;

        log.debug("Sending empty message");

        // Request data
        dataStream = buildStream("");

        // Sends request
        connection.outbound()
            .sendString(dataStream)
            .then()
            // Subscribe to run
            .subscribe();
    }

    @Override
    public final void request(final String message) {
        final Publisher<String> dataStream;

        log.debug("Sending {}", message);

        // Request data
        dataStream = buildStream(message);

        // Sends request
        connection.outbound()
            .sendString(dataStream)
            .then()
            // Subscribe to run
            .subscribe();
    }

    private final Publisher<String> buildStream(final String message) {
        return Mono.just(message)
            .flux()
            .doOnNext(listener::onSend);
    }

}
