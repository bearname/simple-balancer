package ru.mikushov;

import ru.mikushov.app.MainHttpHandler;
import ru.mikushov.balancer.RoundRobinLoadBalancer;
import ru.mikushov.server.Server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    public static final String BODY_HTML = "<html><body><h1>Hello, naive</h1></body></html>";

    public static void main(String[] args) {
        System.out.println(A.getInstance("foo").getS());
        System.out.println(A.getInstance("bar").getS());
        System.out.println(A.getNewInstance("foo").getS());
        System.out.println(A.getNewInstance("bar").getS());
        A instance = A.getInstance("thx");
        System.out.println(instance.getS());
//        MainHttpHandler mainHttpHandler = new MainHttpHandler();
//
//        System.out.println("Usage: <port> | without argument | | <hostname> <port>");
//        Server server;
//        System.out.println(args.length + ": " + args[0]);
//        if (args.length == 1) {
//            int port = Integer.parseInt(args[0]);
//            System.out.println("Usage: <port>" + port);
//            server = new Server(port, mainHttpHandler);
//        } else if (args.length == 2) {
//            String hostname = args[0];
//            int port = Integer.parseInt(args[1]);
//            System.out.println("Usage:  <hostname> " + hostname +" <port>" + port);
//
//            server = new Server(hostname, port, mainHttpHandler);
//        } else {
//            System.out.println("localhost:8080");
//
//            server = new Server(mainHttpHandler);
//        }
//
//        server.bootstrap();
//        new Server((req, resp) -> "<html><body><h1>Hello, naive</h1>It </body></html>").bootstrap();
    }
}

class A {

    private static A instance;

    private String s;

    private A (String s) {
        this.s = s;
    }

    public static A getInstance(String s) {
        if (instance == null) {
            instance = new A(s);
        }
        return instance;
    }

    public static A getNewInstance(String s) {
        return new A(s);
    }

    public String getS() {
        return s;
    }
}