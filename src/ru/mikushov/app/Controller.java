package ru.mikushov.app;

import ru.mikushov.server.MiHttpRequest;
import ru.mikushov.server.MiHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public abstract class Controller {
    MiHttpResponse doGet(MiHttpRequest httpRequest, MiHttpResponse httpResponse) {
        return httpResponse;
    }

    MiHttpResponse doPost(MiHttpRequest httpRequest, MiHttpResponse httpResponse) {
        return httpResponse;
    }

    MiHttpResponse doPut(MiHttpRequest httpRequest, MiHttpResponse httpResponse) {
        return httpResponse;
    }

    MiHttpResponse doDelete(MiHttpRequest httpRequest, MiHttpResponse httpResponse) {
        return httpResponse;
    }

    protected String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
}
