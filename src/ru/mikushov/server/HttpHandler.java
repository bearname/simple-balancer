package ru.mikushov.server;

import ru.mikushov.app.NotFoundException;

public interface HttpHandler {
    String handle(MiHttpRequest request, MiHttpResponse response) throws NotFoundException;
}
