package ru.yandex.praktikum.http;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.praktikum.entity.*;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.utils.LocalDateTimeAdapter;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpStatus.*;

public class HTTPTaskServer {
    public static final int PORT = 8080;
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String HISTORY = "history";
    private final TaskManager manager;
    private final Gson gson;
    private final HttpServer server;

    public HTTPTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        this.gson = getGson();
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handle);
    }

    private void handle(HttpExchange h) throws IOException {
        try {
            String method = h.getRequestMethod();
            InputStream in = h.getRequestBody();
            String path = h.getRequestURI().getPath();
            String query = h.getRequestURI().getQuery();
            String body = new String(in.readAllBytes(), UTF_8);
            String[] params = path.split("/");
            TypeTask type = TypeTask.valueOf(params[2].toUpperCase());
            System.out.printf("%s: %s%n", method, path);
            System.out.printf("%s%n", body);

            switch (method) {
                case GET: {
                    if (params.length == 2) {
                        getPrioritizedTasksHandler(h);
                    } else if (type == TypeTask.TASK) {
                        if (query != null) {
                            getByIdHandler(h, parseId(query));
                        }
                        getHandler(h);
                    } else if (type == TypeTask.EPIC) {
                        if (query != null) {
                            getByIdHandler(h, parseId(query));
                        }
                        getHandler(h);
                    } else if (type == TypeTask.SUBTASK) {
                        if (query != null) {
                            getByIdHandler(h, parseId(query));
                        }
                        getHandler(h);
                    } else if (params[2].equals(HISTORY)) {
                        getHistoryHandler(h);
                    } else {
                        h.sendResponseHeaders(SC_BAD_REQUEST, 0);
                    }
                }
                case POST: {
                    if (type == TypeTask.TASK) {
                        addHandler(h, type, body);
                    } else if (type == TypeTask.EPIC) {
                        addHandler(h, type, body);
                    } else if (type == TypeTask.SUBTASK) {
                        addHandler(h, type, body);
                    } else {
                        h.sendResponseHeaders(SC_BAD_REQUEST, 0);
                    }
                }
                case DELETE: {
                    if (type == TypeTask.TASK) {
                        if (query != null) {
                            deleteByIdHandler(h, parseId(query));
                        }
                        deleteHandler(h);
                    } else if (type == TypeTask.EPIC) {
                        if (query != null) {
                            deleteByIdHandler(h, parseId(query));
                        }
                        deleteHandler(h);
                    } else if (type == TypeTask.SUBTASK) {
                        if (query != null) {
                            deleteByIdHandler(h, parseId(query));
                        }
                        deleteHandler(h);
                    } else {
                        h.sendResponseHeaders(SC_BAD_REQUEST, 0);
                    }
                }
            }
        } catch (Exception e) {
            h.sendResponseHeaders(SC_INTERNAL_SERVER_ERROR, 0);
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    private Class<? extends Task> getTokenTask(TypeTask type) {
        switch (type) {
            case TASK: return Task.class;
            case EPIC: return Epic.class;
            case SUBTASK: return SubTask.class;
            default: throw new IllegalArgumentException();
        }
    }

    private Long parseId(String query) {
        return Long.valueOf(query.split("=")[1]);
    }

    public void addHandler(HttpExchange h, TypeTask type, String body) throws IOException {
        Task task = gson.fromJson(body, getTokenTask(type));
        if (task.getType() == TypeTask.TASK) {
            if (!manager.getTasks().containsKey(task.getId())) {
                manager.addTask(task);
                h.sendResponseHeaders(SC_CREATED, 0);
            } else {
                manager.updateTask(task);
                h.sendResponseHeaders(SC_OK, 0);
            }
            return;
        }
        if (task.getType() == TypeTask.EPIC) {
            if (!manager.getEpics().containsKey(task.getId())) {
                manager.addEpic((Epic) task);
                h.sendResponseHeaders(SC_CREATED, 0);
            } else {
                manager.updateEpic((Epic) task);
                h.sendResponseHeaders(SC_OK, 0);
            }
            return;
        }
        if (task.getType() == TypeTask.SUBTASK) {
            if (!manager.getSubtasks().containsKey(task.getId())) {
                manager.addSubTask((SubTask) task);
                h.sendResponseHeaders(SC_CREATED, 0);
            } else {
                manager.updateSubTask((SubTask) task);
                h.sendResponseHeaders(SC_OK, 0);
            }
            return;
        }
        h.sendResponseHeaders(SC_NOT_FOUND, 0);
    }

    public void getHandler(HttpExchange h) throws IOException {
        if (!manager.getTasks().isEmpty()) {
            sendText(h, gson.toJson(manager.getTasks()));
            return;
        }
        if (!manager.getEpics().isEmpty()) {
            sendText(h, gson.toJson(manager.getEpics()));
            return;
        }
        if (!manager.getSubtasks().isEmpty()) {
            sendText(h, gson.toJson(manager.getSubtasks()));
            return;
        }
        h.sendResponseHeaders(SC_NOT_FOUND, 0);
    }

    public void getByIdHandler(HttpExchange h, Long id) throws IOException {
        if (manager.getTasks().containsKey(id)) {
            Task task = manager.getTaskById(id);
            sendText(h, gson.toJson(task));
            return;
        }
        if (manager.getEpics().containsKey(id)) {
            Epic epic = manager.getEpicById(id);
            sendText(h, gson.toJson(epic));
            return;
        }
        if (manager.getSubtasks().containsKey(id)) {
            SubTask subTask = manager.getSubTaskById(id);
            sendText(h, gson.toJson(subTask));
            return;
        }
        h.sendResponseHeaders(SC_NOT_FOUND, 0);
    }

    public void deleteHandler(HttpExchange h) throws IOException {
        if (!manager.getTasks().isEmpty()) {
            manager.deleteAllTask();
            h.sendResponseHeaders(SC_OK, 0);
            return;
        }
        if (!manager.getEpics().isEmpty()) {
            manager.deleteAllEpic();
            h.sendResponseHeaders(SC_OK, 0);
            return;
        }
        if (!manager.getSubtasks().isEmpty()) {
            manager.deleteAllSubTask(null);
            h.sendResponseHeaders(SC_OK, 0);
            return;
        }
        h.sendResponseHeaders(SC_NOT_FOUND, 0);
    }

    public void deleteByIdHandler(HttpExchange h, Long id) throws IOException {
        if (manager.getTasks().containsKey(id)) {
            manager.deleteTaskById(id);
            h.sendResponseHeaders(SC_OK, 0);
            return;
        }
        if (manager.getEpics().containsKey(id)) {
            manager.deleteEpicById(id);
            h.sendResponseHeaders(SC_OK, 0);
            return;
        }
        if (manager.getSubtasks().containsKey(id)) {
            manager.deleteSubTaskById(id);
            h.sendResponseHeaders(SC_OK, 0);
            return;
        }
        h.sendResponseHeaders(SC_NOT_FOUND, 0);
    }

    public void getHistoryHandler(HttpExchange h) throws IOException {
        if (!manager.getHistory().isEmpty()) {
            sendText(h, gson.toJson(manager.getHistory()));
            return;
        }
        h.sendResponseHeaders(SC_NOT_FOUND, 0);
    }

    public void getPrioritizedTasksHandler(HttpExchange h) throws IOException {
        if (!manager.getPrioritizedTasks().isEmpty()) {
            sendText(h, gson.toJson(manager.getPrioritizedTasks()));
            return;
        }
        h.sendResponseHeaders(SC_NOT_FOUND, 0);
    }

    public static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return builder.create();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(SC_OK, resp.length);
        h.getResponseBody().write(resp);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }
}
