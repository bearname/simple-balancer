package ru.mikushov.app;

public class NotFoundException extends Throwable {
    public NotFoundException(String s) {
        super(s);
    }
}
