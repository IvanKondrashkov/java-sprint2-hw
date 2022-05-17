package ru.yandex.praktikum.utils;

import java.io.File;
import ru.yandex.praktikum.manager.*;

public class Managers {
    private static final File file = new File("src/main/resources/data.csv");

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTasksManager() {
        return new FileBackedTasksManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
