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

package com.bernardomg.example.netty.tcp.cli.command;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import com.bernardomg.example.netty.tcp.cli.CliWriterTransactionListener;
import com.bernardomg.example.netty.tcp.cli.version.ManifestVersionProvider;
import com.bernardomg.example.netty.tcp.client.ReactorNettyTcpClient;
import com.bernardomg.example.netty.tcp.client.TransactionListener;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

/**
 * Send empty message command. Will send an empty message to the server through TCP.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Command(name = "empty", description = "Sends an empty TCP message", mixinStandardHelpOptions = true,
        versionProvider = ManifestVersionProvider.class)
@Slf4j
public final class SendEmptyMessageCommand implements Runnable {

    /**
     * Server host.
     */
    @Parameters(index = "0", description = "Server host.", paramLabel = "HOST")
    private String      host;

    /**
     * Server port.
     */
    @Parameters(index = "1", description = "Server port.", paramLabel = "PORT")
    private Integer     port;

    /**
     * Command specification. Used to get the line output.
     */
    @Spec
    private CommandSpec spec;

    /**
     * Verbose mode. If active prints info into the console. Active by default.
     */
    @Option(names = { "--verbose" }, paramLabel = "flag", description = "Print information to console.",
            defaultValue = "true", showDefaultValue = Help.Visibility.ALWAYS)
    private Boolean     verbose;

    /**
     * Response wait time. This is the number of seconds to wait for responses.
     */
    @Option(names = { "--wait" }, paramLabel = "seconds", description = "Wait received seconds, to wait for responses.",
            defaultValue = "2", showDefaultValue = Help.Visibility.ALWAYS)
    private Integer     wait;

    /**
     * Response wait time. This is the number of seconds to wait for responses.
     */
    @Option(names = { "--wiretap" }, paramLabel = "flag", description = "Enable wiretap logging",
            defaultValue = "false")
    private Boolean     wiretap;

    /**
     * Default constructor.
     */
    public SendEmptyMessageCommand() {
        super();
    }

    @Override
    public final void run() {
        final PrintWriter           writer;
        final ReactorNettyTcpClient client;
        final TransactionListener   listener;

        if (verbose) {
            // Prints to console
            writer = spec.commandLine()
                .getOut();
        } else {
            // Prints nothing
            writer = new PrintWriter(OutputStream.nullOutputStream(), false, Charset.defaultCharset());
        }

        // Create client
        listener = new CliWriterTransactionListener(host, port, writer);
        client = new ReactorNettyTcpClient(host, port, listener);
        client.setWiretap(wiretap);

        client.connect();

        // Send message
        client.request();

        // Give time to the server for responses
        log.debug("Waiting {} seconds for responses", wait);
        writer.printf("Waiting %d seconds for responses", wait);
        writer.println();
        try {
            TimeUnit.SECONDS.sleep(wait);
        } catch (final InterruptedException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
        writer.println("finished waiting");
        log.debug("Finished waiting for responses");

        // close client
        client.close();

        // Close writer
        writer.close();
    }

}
