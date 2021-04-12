package ru.mikushov.server;

public class HttpStatus {
    private int code;
    private String message;
    private String description;

    public HttpStatus(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
