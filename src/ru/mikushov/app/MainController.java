package ru.mikushov.app;

import ru.mikushov.server.MiHttpRequest;
import ru.mikushov.server.MiHttpResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class MainController extends Controller {
    @Override
    public MiHttpResponse doGet(MiHttpRequest httpRequest, MiHttpResponse httpResponse) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://openjdk.java.net/"))
                .build();
        CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        httpResponseCompletableFuture.thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
//        String body = readLineByLineJava8("C:\\Users\\mikha\\Desktop\\github\\simpleserver\\resources\\index.html");
        String body = "";
        HttpResponse<String> stringHttpResponse = null;
        try {
            stringHttpResponse = httpResponseCompletableFuture.get();
            body = stringHttpResponse.body();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            body = readLineByLineJava8("C:\\Users\\mikha\\Desktop\\github\\simpleserver\\resources\\index.html");
        }

        httpResponse.setBody(body);
        return httpResponse;
    }
}
