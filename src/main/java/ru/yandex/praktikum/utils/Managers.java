package ru.yandex.praktikum.utils;

import ru.yandex.praktikum.manager.HistoryManager;
import ru.yandex.praktikum.manager.InMemoryHistoryManager;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.manager.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
