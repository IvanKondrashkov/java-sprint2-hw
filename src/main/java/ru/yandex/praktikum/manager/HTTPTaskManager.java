package ru.yandex.praktikum.manager;

import java.util.Map;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.praktikum.entity.Task;
import ru.yandex.praktikum.entity.Epic;
import ru.yandex.praktikum.entity.SubTask;
import ru.yandex.praktikum.http.HTTPTaskServer;
import ru.yandex.praktikum.http.KVClient;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVClient client;
    private final Gson gson;

    public HTTPTaskManager(KVClient client) {
        super(null);
        this.client = client;
        this.gson = HTTPTaskServer.getGson();
        load();
    }

    @Override
    protected void save() {
        String json = gson.toJson(tasks);
        if (!json.isEmpty()) {
            client.put("tasks", json);
        }

        json = gson.toJson(epics);
        if (!json.isEmpty()) {
            client.put("epics", json);
        }

        json = gson.toJson(subtasks);
        if (!json.isEmpty()) {
            client.put("subtasks", json);
        }

        json = gson.toJson(historyManager.getHistory());
        if (!json.isEmpty()) {
            client.put("history", json);
        }
    }

    @Override
    protected void load() {
        String json = client.load("tasks");
        if (!json.isEmpty()) {
            tasks = gson.fromJson(json, new TypeToken<Map<Long, Task>>() {
            }.getType());
        }

        json = client.load("epics");
        if (!json.isEmpty()) {
            epics = gson.fromJson(json, new TypeToken<Map<Long, Epic>>() {
            }.getType());
        }

        json = client.load("subtasks");
        if (!json.isEmpty()) {
            subtasks = gson.fromJson(json, new TypeToken<Map<Long, SubTask>>() {
            }.getType());
        }

        json = client.load("history");
        if (!json.isEmpty()) {
            List<Task> history = gson.fromJson(json, new TypeToken<List<Task>>() {
            }.getType());

            for (Task it : history) {
                if (epics.containsKey(it.getId())) {
                    Epic epic = epics.get(it.getId());
                    historyManager.addTask(epic);
                }
                if (tasks.containsKey(it.getId())) {
                    Task task = tasks.get(it.getId());
                    historyManager.addTask(task);
                }
                if (subtasks.containsKey(it.getId())) {
                    SubTask subTask = subtasks.get(it.getId());
                    historyManager.addTask(subTask);
                }
            }
        }
    }
}
