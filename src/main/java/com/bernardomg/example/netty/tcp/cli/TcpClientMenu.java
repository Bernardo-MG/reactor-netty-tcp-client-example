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

package com.bernardomg.example.netty.tcp.cli;

import com.bernardomg.example.netty.tcp.cli.command.SendEmptyMessageCommand;
import com.bernardomg.example.netty.tcp.cli.command.SendMessageCommand;
import com.bernardomg.example.netty.tcp.cli.version.ManifestVersionProvider;

import picocli.CommandLine.Command;

/**
 * TCP client menu.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Command(description = "TCP client", subcommands = { SendMessageCommand.class, SendEmptyMessageCommand.class },
        mixinStandardHelpOptions = true, versionProvider = ManifestVersionProvider.class)
public class TcpClientMenu {

    /**
     * Default constructor.
     */
    public TcpClientMenu() {
        super();
    }

}
