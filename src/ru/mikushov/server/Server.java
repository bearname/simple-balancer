package ru.mikushov.server;

import ru.mikushov.app.NotFoundException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class Server {
    public static final int BUFFER_SIZE = 256;

    private AsynchronousServerSocketChannel server;

    private HttpHandler httpHandler;
    private int port = 8080;
    private String hostname = "127.0.0.1";

    public Server(String hostname, int port, HttpHandler httpHandler) {
        this.port = port;
        this.hostname = hostname;
        this.httpHandler = httpHandler;
    }

    public Server(int port, HttpHandler httpHandler) {
        this.port = port;
        this.httpHandler = httpHandler;

    }

    public int getPort() {
        return port;
    }

    public String getHostname() {
        return hostname;
    }

    public Server(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public void bootstrap() {
        try {
            server = AsynchronousServerSocketChannel.open();

            InetSocketAddress local = new InetSocketAddress(hostname, port);
            System.out.println(local.toString());
            server.bind(local);

            while (true) {
                Future<AsynchronousSocketChannel> future = server.accept();
                handleClient(future);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Future<AsynchronousSocketChannel> future) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        System.out.println("new client connections");

        AsynchronousSocketChannel clientChannel = future.get();

        while (clientChannel != null && clientChannel.isOpen()) {
            try {
                MiHttpRequest httpRequest = new MiHttpRequest(getRequestString(clientChannel));
                MiHttpResponse httpResponse = buildHttpResponse(httpRequest);

                writeResponseToSocket(clientChannel, httpResponse);
            } catch (Exception e) {
                e.printStackTrace();

                MiHttpResponse httpResponse = new MiHttpResponse();
                httpResponse.sendError("Internal ru.mikushov.server error",
                        500,
                        "Internal ru.mikushov.server error");


                System.out.println(httpResponse.message());
                writeResponseToSocket(clientChannel, httpResponse);
            }
        }
    }

    private MiHttpResponse buildHttpResponse(MiHttpRequest httpRequest) {
        MiHttpResponse httpResponse = new MiHttpResponse();

        if (httpHandler != null) {
            try {
                String body = this.httpHandler.handle(httpRequest, httpResponse);

                body += this.toString() + "</h1></body></html>";
                ;
                if (body != null && !body.isBlank()) {

                    if (httpResponse.getHeaders().get("Content-Type") == null) {
                        httpResponse.addHeader("Content-Type", "text/html; charset=utf-8");
                    }

                    httpResponse.setBody(body);
                }
            } catch (Exception e) {
                e.printStackTrace();

                httpResponse.sendError("Error happens" + this.toString(), 500, "Internal server error");
            } catch (NotFoundException e) {

                httpResponse.sendError("Resource not found" + this.toString() + "\n request url" + httpRequest.getUrl(), 404, "Resource not found");
                e.printStackTrace();
            }
        } else {
            httpResponse.sendError("Resource not found", 404, "Resource not found" + this.toString());

        }

        return httpResponse;
    }

    private String getRequestString(AsynchronousSocketChannel clientChannel) throws InterruptedException, ExecutionException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        StringBuilder builder = new StringBuilder();
        boolean keepReading = true;

        while (keepReading) {
            int readResult = clientChannel.read(buffer).get();

            keepReading = readResult == BUFFER_SIZE;

            buffer.flip();

            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);

            builder.append(charBuffer);
            buffer.clear();
        }

        return builder.toString();
    }

    private void writeResponseToSocket(AsynchronousSocketChannel clientChannel, MiHttpResponse httpResponse) throws IOException {
        ByteBuffer response = ByteBuffer.wrap(httpResponse.getBytes());

        clientChannel.write(response);
        clientChannel.close();
    }

    @Override
    public String toString() {
        return "Server{" +
                "hostname=" + hostname +
                ":'" + port + '\'' +
                '}';
    }
}