package ru.yandex.praktikum.manager;

import java.io.File;
import ru.yandex.praktikum.http.KVClient;
import ru.yandex.praktikum.http.KVServer;

public class Managers {
    public static TaskManager getDefault() {
        return new HTTPTaskManager(new KVClient(KVServer.PORT));
    }

    public static TaskManager getFileBackedTasksManager(File file) {
        return new FileBackedTasksManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
