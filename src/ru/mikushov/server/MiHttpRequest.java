package ru.mikushov.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MiHttpRequest {

    private final static String DELIMITER = "\r\n\r\n";
    private final static String NEW_LINE = "\r\n";
    public static final String HEADER_DELIMITER = ":";

    private final String message;
    private final HttpMethod httpMethod;
    private final String url;
    private final String body;
    private final Map<String, String> headers;

    public MiHttpRequest(String message) throws Exception {
        this.message = message;

        String[] parts = message.split(DELIMITER);

        String head = parts[0];
        String[] headers = head.split(NEW_LINE);

        String[] firstLine = headers[0].split(" ");
        for (int i = 0; i < firstLine.length; i++) {
            System.out.println("url: '" + i + "'" + firstLine[i]);
        }
        if (firstLine.length < 2) {
            throw new Exception("invalid request");
        }

        httpMethod = HttpMethod.valueOf(firstLine[0].isBlank() ? "GET" : firstLine[0]);
        url = firstLine[1];
        System.out.println("url: '" + url + "'");
        this.headers = Collections.unmodifiableMap(
                new HashMap<>() {{
                    for (int i = 1; i < headers.length; i++) {
                        String[] headerPart = headers[i].split(HEADER_DELIMITER, 2);
                        put(headerPart[0].trim(), headerPart[1].trim());
                    }
                }}
        );

        String bodyLength = this.headers.get("Content-Length");
        int contentLength = bodyLength != null ? Integer.parseInt(bodyLength) : 0;
        this.body = parts.length > 1 ? parts[1].trim().substring(0, contentLength) : "";
    }

    public String getMessage() {
        return message;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "MiHttpRequest{" +
                "url='" + url + '\'' +
                '}';
    }
}
