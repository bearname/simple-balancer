package ru.mikushov.server;

import java.util.*;

public class MiHttpResponse {
    private final static String NEW_LINE = "\r\n";

    private Map<String, String> headers = new HashMap<>();
    private String body = "";
    private int statusCode = 200;
    private String statusText = "OK";

    private final static List<HttpStatus> HTTP_STATUS_CODES = new ArrayList<>(Arrays.asList(
            new HttpStatus(100, "Continue", "Only a part of the request has been received by the server, but as long as it has not been rejected, the client should continue with the request"),
            new HttpStatus(101, "Switching Protocols", "The server switches protocol."),
            new HttpStatus(200, "OK", "The request is OK"),
            new HttpStatus(201, "Created", "The request is complete, and a new resource is created"),
            new HttpStatus(202, "Accepted", "The request is accepted for processing, but the processing is not complete."),
            new HttpStatus(203, "Non-authoritative Information", ""),
            new HttpStatus(204, "No Content", ""),
            new HttpStatus(205, "Reset Content", ""),
            new HttpStatus(206, "Partial Content", ""),
            new HttpStatus(300, "Multiple Choices", "A link list. The user can select a link and go to that location. Maximum five addresses"),
            new HttpStatus(301, "Moved Permanently", "The requested page has moved to a new url"),
            new HttpStatus(302, "Found", "The requested page has moved temporarily to a new url"),
            new HttpStatus(303, "See Other", "The requested page can be found under a different url"),
            new HttpStatus(304, "Not Modified", ""),
            new HttpStatus(305, "Use Proxy", ""),
            new HttpStatus(307, "Temporary Redirect", "The requested page has moved temporarily to a new url."),
            new HttpStatus(400, "Bad Request", "The server did not understand the reques"),
            new HttpStatus(401, "Unauthorized", "The requested page needs a username and a password"),
            new HttpStatus(402, "Payment Required", "You cannot use this code yet"),
            new HttpStatus(403, "Forbidden", "Access is forbidden to the requested page"),
            new HttpStatus(404, "Not Found", "The server cannot find the requested page"),
            new HttpStatus(405, "Method Not Allowed", "The method specified in the request is not allowed."),
            new HttpStatus(406, "Not Acceptable", "The server can only generate a response that is not accepted by the client."),
            new HttpStatus(407, "Proxy Authentication Required", "You must authenticate with a proxy server before this request can be served."),
            new HttpStatus(408, "Request Timeout", "The request took longer than the server was prepared to wait."),
            new HttpStatus(409, "Conflict", "The request could not be completed because of a conflict."),
            new HttpStatus(410, "Gone", "The requested page is no longer available."),
            new HttpStatus(411, "Length Required", "The \"Content-Length\" is not defined. The server will not accept the request without it."),
            new HttpStatus(412, "Precondition Failed", "The precondition given in the request evaluated to false by the server."),
            new HttpStatus(413, "Request Entity Too Large", "The server will not accept the request, because the request entity is too large."),
            new HttpStatus(414, "Request-url Too Long", "The server will not accept the request, because the url is too long. Occurs when you convert a \"post\" request to a \"get\" request with a long query information."),
            new HttpStatus(415, "Unsupported Media Type", "The server will not accept the request, because the media type is not supported."),
            new HttpStatus(417, "Expectation Failed", ""),
            new HttpStatus(500, "Internal Server Error", "The request was not completed. The server met an unexpected condition."),
            new HttpStatus(501, "Not Implemented", "The request was not completed. The server did not support the functionality required."),
            new HttpStatus(502, "Bad Gateway", "The request was not completed. The server received an invalid response from the upstream server."),
            new HttpStatus(503, "Service Unavailable", "The request was not completed. The server is temporarily overloading or down."),
            new HttpStatus(504, "Gateway Timeout\t", "The gateway has timed out."),
            new HttpStatus(504, "HTTP Version Not Supported", "The server does not support the \"http protocol\" version.")
    ));

    public MiHttpResponse() {
        addHeader("Server", "mi");
        addHeader("Connection", "Close");
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    public void setCookie(String key, String value) {
        String cookie = getHeader("Set-Cookie");

        cookie += " " + key + "=" + value + ";";

        this.headers.put("Set-Cookie", cookie);
    }

    public void sendError(final String htmlMessage, int statusCode, String statusText) {
        setStatusCode(statusCode);
        setStatusText(statusText);
        addHeader("Content-Type", "text/html; charset=utf-8");
        setBody("<html><body><h1>" + htmlMessage + "</body></html>");
    }

    public void sendError(HttpStatus httpStatus) {
        sendError(httpStatus.getDescription(), httpStatus.getCode(), httpStatus.getMessage());
    }

    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }

    public String getHeader(String header) {
        return headers.getOrDefault(header, "");
    }

    public String message() {
        StringBuilder builder = new StringBuilder();

        builder.append("HTTP/1.1")
                .append(" ")
                .append(statusCode)
                .append(" ")
                .append(statusText)
                .append(NEW_LINE);

        for (Map.Entry<String, String> stringStringEntry : headers.entrySet()) {
            builder.append(stringStringEntry.getKey())
                    .append(": ")
                    .append(stringStringEntry.getValue())
                    .append(NEW_LINE);

        }

        return builder
                .append(NEW_LINE)
                .append(body)
                .toString();
    }

    public byte[] getBytes() {

        String message = message();
        return message.getBytes();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        setContentLength(body);
        this.body = body;
    }

    private void setContentLength(String body) {
        this.headers.put("Content-Length", String.valueOf(body.length()));
    }

    public void appendToBody(String body) {
        this.body += body;
        setContentLength(body);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String status) {
        this.statusText = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void sendRedirect(String redirectUrl) {
        addHeader("Location", redirectUrl);
    }
}