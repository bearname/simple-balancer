package ru.mikushov.app;

import ru.mikushov.server.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MainHttpHandler implements HttpHandler {
    public static final String RESOURCE_PATH = "C:\\Users\\mikha\\Desktop\\github\\simpleserver\\resources\\";
    private static String SUPPORTED_FILE_EXTENSION_PATTERN = "\\.(jpeg|jpg|gif|png)";

    private static Pattern SUPPORTED_STATIC_FILE_REGEX = Pattern.compile(SUPPORTED_FILE_EXTENSION_PATTERN, Pattern.CASE_INSENSITIVE);

    private String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    @Override
    public String handle(MiHttpRequest miHttpRequest, MiHttpResponse miHttpResponse) throws NotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://openjdk.java.net/"))
                .build();
        CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        httpResponseCompletableFuture.thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
        String body = "";
        HttpResponse<String> stringHttpResponse = null;
        try {
            stringHttpResponse = httpResponseCompletableFuture.get();
            body = stringHttpResponse.body();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            body = readLineByLineJava8("C:\\Users\\mikha\\Desktop\\github\\simpleserver\\resources\\index.html");
        }

        return body;
//        miHttpRequest.getHeaders().forEach((key, value) -> {
//            System.out.println(key + ":" + value);
//        });
//
//
//        return "<html><body><h1>Hello, naive" + miHttpRequest.toString();
//
//        HttpMethod httpMethod = miHttpRequest.getHttpMethod();
//        String responseData = "";
//
//        if (httpMethod == HttpMethod.GET) {
//            String url = miHttpRequest.getUrl();
//            Matcher matcher = SUPPORTED_STATIC_FILE_REGEX.matcher(url);
//            if (matcher.find()) {
//                responseData = readLineByLineJava8(RESOURCE_PATH + "image.jpg");
//                miHttpResponse.setBody(responseData);
//                miHttpResponse.addHeader("Accept-Ranges", "bytes");
//                miHttpResponse.addHeader("Cache-Control", " no-cache, no-store, max-age=0, must-revalidate");
//                miHttpResponse.addHeader("Pragma", " no-cache");
//                miHttpResponse.addHeader("Expires", " no-cache");
//                miHttpResponse.addHeader("Content-Type", MediaType.JPEG.toString());
//                miHttpResponse.setBody(responseData);
//                return responseData;
//            } else {
//                String resourceFilePath = RESOURCE_PATH;
//                switch (url) {
//                    case "/index":
//                    case "/":
//                        responseData = new MainController().doGet(miHttpRequest, miHttpResponse).getBody();
//                        return responseData;
//                    case "/profile":
//                        resourceFilePath += "profile.html";
//                        break;
//                    case "/login":
//                        resourceFilePath += "login.html";
//                        break;
//                    default:
//                        resourceFilePath += "index.html";
//                        throw new NotFoundException("Not found url " + miHttpRequest.getUrl());
//                }
//
//                responseData = readLineByLineJava8(resourceFilePath);
//            }
//        }
//
//        return responseData;
    }
}
