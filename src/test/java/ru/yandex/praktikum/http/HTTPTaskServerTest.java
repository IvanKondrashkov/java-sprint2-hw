package ru.yandex.praktikum.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.yandex.praktikum.entity.*;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.HTTPTaskManager;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskServerTest {
    private KVServer storage;
    private HTTPTaskManager manager;
    private HTTPTaskServer server;
    private HttpClient client;
    private Gson gson;
    private Task task;
    private Epic epic;
    private SubTask subTask;
    private LocalDateTime currentTime = LocalDateTime.now();

    @BeforeEach
    void init() throws IOException {
        storage = new KVServer();
        storage.start();
        manager = (HTTPTaskManager) Managers.getDefault();
        server = new HTTPTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();

        gson = HTTPTaskServer.getGson();
        task = new Task(TypeTask.TASK, "First task", "Prepare food", Status.NEW, 30, currentTime);
        epic = new Epic(TypeTask.EPIC, "First epic", "Clean the room", Status.NEW, 0, LocalDateTime.now());
        currentTime = currentTime.plusMinutes(task.getDuration());
        subTask = new SubTask(TypeTask.SUBTASK, "First subtask", "Wash the floors", Status.NEW, 30, currentTime, epic.getId());
    }

    @AfterEach
    void tearDown() {
        storage.stop();
        server.stop();
    }

    @EnumSource(TypeTask.class)
    @ParameterizedTest
    @DisplayName("Send post request task, epic, subtask")
    void addHandler(TypeTask type) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase());
        String json = null;

        if (type == task.getType()) {
            json = gson.toJson(task);
        }
        if (type == epic.getType()) {
            json = gson.toJson(epic);
        }
        if (type == subTask.getType()) {
            epic = manager.addEpic(epic);
            subTask.setEpicId(epic.getId());
            json = gson.toJson(subTask);
        }

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        int actual = response.statusCode();

        assertEquals(SC_CREATED, actual);
    }

    @EnumSource(TypeTask.class)
    @ParameterizedTest
    @DisplayName("Send post request task, epic, subtask")
    void updateHandler(TypeTask type) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase());
        String json = null;
        task = manager.addTask(task);
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);

        if (type == task.getType()) {
            json = gson.toJson(task);
        }
        if (type == epic.getType()) {
            json = gson.toJson(epic);
        }
        if (type == subTask.getType()) {
            json = gson.toJson(subTask);
        }

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        int actual = response.statusCode();

        assertEquals(SC_OK, actual);
    }

    @EnumSource(TypeTask.class)
    @ParameterizedTest
    @DisplayName("Send get request task, epic, subtask")
    void getHandler(TypeTask type) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase());
        task = manager.addTask(task);
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int actual = response.statusCode();

        assertEquals(SC_OK, actual);
    }

    @EnumSource(TypeTask.class)
    @ParameterizedTest
    @DisplayName("Send get request task, epic, subtask by id")
    void getByIdHandler(TypeTask type) throws IOException, InterruptedException {
        URI url = null;
        task = manager.addTask(task);
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);

        if (type == task.getType()) {
            url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase() + "/?id=" + task.getId());
        }
        if (type == epic.getType()) {
            url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase() + "/?id=" + epic.getId());
        }
        if (type == subTask.getType()) {
            url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase() + "/?id=" + subTask.getId());
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int actual = response.statusCode();

        assertEquals(SC_OK, actual);
    }

    @EnumSource(TypeTask.class)
    @ParameterizedTest
    @DisplayName("Send delete request task, epic, subtask")
    void deleteHandler(TypeTask type) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase());
        task = manager.addTask(task);
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        int actual = response.statusCode();

        assertEquals(SC_OK, actual);
    }

    @EnumSource(TypeTask.class)
    @ParameterizedTest
    @DisplayName("Send delete request task, epic, subtask by id")
    void deleteByIdHandler(TypeTask type) throws IOException, InterruptedException {
        URI url = null;
        task = manager.addTask(task);
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);

        if (type == task.getType()) {
            url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase() + "/?id=" + task.getId());
        }
        if (type == epic.getType()) {
            url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase() + "/?id=" + epic.getId());
        }
        if (type == subTask.getType()) {
            url = URI.create("http://localhost:8080/tasks/" + type.name().toLowerCase() + "/?id=" + subTask.getId());
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        int actual = response.statusCode();

        assertEquals(SC_OK, actual);
    }

    @Test
    @DisplayName("Send get request history")
    void getHistoryHandler() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/history");
        task = manager.addTask(task);
        task = manager.getTaskById(task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int actual = response.statusCode();

        assertEquals(SC_OK, actual);
    }

    @Test
    @DisplayName("Send get request prioritized tasks list")
    void getPrioritizedTasksHandler() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/");
        task = manager.addTask(task);
        task = manager.getTaskById(task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int actual = response.statusCode();

        assertEquals(SC_OK, actual);
    }
}
