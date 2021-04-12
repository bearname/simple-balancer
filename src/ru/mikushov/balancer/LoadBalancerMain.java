package ru.mikushov.balancer;

import ru.mikushov.app.MainHttpHandler;
import ru.mikushov.server.HttpHandler;
import ru.mikushov.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static ru.mikushov.Main.BODY_HTML;

public class LoadBalancerMain {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: run-balancer.bat <port> ...");
            System.out.println("Add some available port");
        }

        List<Integer> usedPorts = new ArrayList<>();

        Arrays.stream(args).forEach(item -> {
            usedPorts.add(Integer.parseInt(item));
        });

        usedPorts.forEach(System.out::println);

        List<String> ipList = new ArrayList<>();

        Runtime runtime = Runtime.getRuntime();
        Map<String, Integer> ipMap = new HashMap<>();
        Map<String, Integer> requestStatistic = new HashMap<>();
        Random random = new Random();

        for (String port : args) {
            System.out.println(port);
            String host = "127.0.0.1:" + port;
            ipList.add(host);
            ipMap.put(host, random.nextInt(4));
            requestStatistic.put(host, 0);
            Process process;
            try {
//                String command = "\"C:\\Program Files\\Java\\jdk-11.0.4\\bin\\java.exe\" \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2020.2.3\\lib\\idea_rt.jar=48733:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2020.2.3\\bin\" -Dfile.encoding=UTF-8 -classpath C:\\Users\\mikha\\Desktop\\github\\simpleserver\\out\\production\\simpleserver ru.mikushov.Main " + port;
                String command = "\"C:\\Program Files\\Java\\jdk-11.0.4\\bin\\java.exe\" \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2020.2.3\\lib\\idea_rt.jar=61871:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2020.2.3\\bin\" -Dfile.encoding=UTF-8 -classpath C:\\Users\\mikha\\Desktop\\github\\simpleserver\\out\\production\\simpleserver ru.mikushov.Main " + port;
                System.out.println(command);
                process = runtime.exec(command);
//                printResults(process);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.out.println("Servers");
        printMap(ipMap);

        ipList.forEach(System.out::println);
        System.out.println("end");

        WeightedRoundRobinLoadBalancer robinLoadBalancer = new WeightedRoundRobinLoadBalancer(ipMap);

        HttpHandler httpHandler = (request, response) -> {

            try {
                String ip = robinLoadBalancer.getIp();
                if (requestStatistic.containsKey(ip)) {
                    requestStatistic.put(ip, requestStatistic.get(ip) + 1);
                    System.out.println("ipMap");
                    printMap(ipMap);
                    System.out.println("Request statistic");
                    printMap(requestStatistic);
                }
                System.out.println("load balancer to " + ip);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest httpClientRequest = HttpRequest.newBuilder()
                        .uri(URI.create("http://" + ip + request.getUrl()))
                        .build();

                CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = client.sendAsync(httpClientRequest, HttpResponse.BodyHandlers.ofString());
                httpResponseCompletableFuture.thenApply(HttpResponse::body)
                        .thenAccept(System.out::println)
                        .join();

                HttpResponse<String> stringHttpResponse = httpResponseCompletableFuture.get();
                String body = stringHttpResponse.body();
                response.setBody(body);

                return body;
            } catch (InterruptedException e) {
                response.sendError("Resource not found", 500, "Resource not found");
                e.printStackTrace();
            } catch (ExecutionException e) {
                response.sendError("Resource not found", 500, "Resource not found");
                e.printStackTrace();
            }
            return BODY_HTML;
        };

        new Server(httpHandler).bootstrap();
    }

    private static void printMap(Map<String, Integer> ipMap) {
        ipMap.forEach((key, value) -> {
            System.out.println(key + " : " + value);
        });
    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
