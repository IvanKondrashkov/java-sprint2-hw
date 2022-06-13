package ru.yandex.praktikum.http;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpStatus.*;

public class KVServer {
    public static final int PORT = 8078;
    public static final String GET = "GET";
    public static final String POST = "POST";
    private final String token;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        this.token = generateApiToken();
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/register");
            if (GET.equals(h.getRequestMethod())) {
                sendText(h, token);
            } else {
                System.out.println("/register waiting for a GET request, but received: " + h.getRequestMethod());
                h.sendResponseHeaders(SC_METHOD_NOT_ALLOWED, 0);
            }
        } finally {
            h.close();
        }
    }

    private void save(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/save");
            if (!hasAuth(h)) {
                System.out.println("The request is unauthorized, you need a parameter in the query API_TOKEN with the value of the api key");
                h.sendResponseHeaders(SC_FORBIDDEN, 0);
                return;
            }
            if (POST.equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key to keep empty. The key is specified in the path: /save/{key}");
                    h.sendResponseHeaders(SC_BAD_REQUEST, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Value to keep empty. Value is specified in the request body");
                    h.sendResponseHeaders(SC_BAD_REQUEST, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("Value for the key " + key + " successfully updated!");
                h.sendResponseHeaders(SC_OK, 0);
            } else {
                System.out.println("/save waiting for a POST request, but received: " + h.getRequestMethod());
                h.sendResponseHeaders(SC_METHOD_NOT_ALLOWED, 0);
            }
        } finally {
            h.close();
        }
    }

    private void load(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/load");
            if (!hasAuth(h)) {
                System.out.println("The request is unauthorized, you need a parameter in the query API_TOKEN with the value of the api key");
                h.sendResponseHeaders(SC_FORBIDDEN, 0);
                return;
            }
            if (GET.equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/load/".length());
                if (!data.containsKey(key)) {
                    System.out.println("The key to extract is empty. The key is specified in the path: /load/{key}");
                    h.sendResponseHeaders(SC_BAD_REQUEST, 0);
                    return;
                }
                String value = data.get(key);
                sendText(h, value);
                System.out.println("Value " + value + " successfully extracted!");
                h.sendResponseHeaders(SC_OK, 0);
            } else {
                System.out.println("/load waiting for a GET request, but received: " + h.getRequestMethod());
                h.sendResponseHeaders(SC_METHOD_NOT_ALLOWED, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Starting the server on the port " + PORT);
        System.out.println("Open in the browser http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + token);
        server.start();
    }

    public void stop() {
        System.out.println("Stopping the server");
        server.stop(1);
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + token) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(SC_OK, resp.length);
        h.getResponseBody().write(resp);
    }

    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}
