package ru.yandex.praktikum.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import ru.yandex.praktikum.exception.ManagerSaveException;

public class KVClient {
    private final int port;
    private final HttpClient client;
    private final HttpRequest.Builder builder;
    private final String token;

    public KVClient(int port) {
        this.port = port;
        this.client = HttpClient.newHttpClient();
        this.builder = HttpRequest.newBuilder();
        this.token = register();
    }

    private String register() {
        HttpRequest request = builder
                .uri(URI.create("http://localhost:" + port + "/register"))
                .GET()
                .build();

        HttpResponse<String> response = null;
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (NullPointerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    public void put(String key, String json) {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = builder
                .uri(URI.create("http://localhost:" + port + "/save/" + key + "?API_TOKEN=" + token))
                .POST(body)
                .build();

        HttpResponse<String> response = null;
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (NullPointerException | IOException | InterruptedException e) {
            throw new ManagerSaveException("Saving to server ended incorrectly!", e);
        }
    }

    public String load(String key) {
        HttpRequest request = builder
                .uri(URI.create("http://localhost:" + port + "/load/" + key + "?API_TOKEN=" + token))
                .GET()
                .build();

        HttpResponse<String> response = null;
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
        } catch (NullPointerException | IOException | InterruptedException e) {
            throw new ManagerSaveException("The download from the server ended incorrectly!", e);
        }
        return response.body();
    }
}
