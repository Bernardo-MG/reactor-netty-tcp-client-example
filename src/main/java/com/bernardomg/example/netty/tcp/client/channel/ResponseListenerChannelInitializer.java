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

package com.bernardomg.example.netty.tcp.client.channel;

import java.util.Objects;
import java.util.function.BiConsumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Initializes the channel with a response listener.
 *
 * @author bernardo.martinezg
 *
 */
@Slf4j
public final class ResponseListenerChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * Response listener. This will receive any response from the channel.
     */
    private final BiConsumer<ChannelHandlerContext, String> responseListener;

    public ResponseListenerChannelInitializer(final BiConsumer<ChannelHandlerContext, String> listener) {
        super();

        responseListener = Objects.requireNonNull(listener);
    }

    @Override
    protected final void initChannel(final SocketChannel ch) throws Exception {
        final ResponseListenerChannelHandler listenerHandler;

        listenerHandler = new ResponseListenerChannelHandler(responseListener);

        log.debug("Initializing channel");

        ch.pipeline()
            .addLast("decoder", new StringDecoder())
            .addLast(listenerHandler);

        log.debug("Initialized channel");
    }

}
