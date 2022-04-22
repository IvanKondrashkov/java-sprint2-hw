package ru.yandex.praktikum.manager;

import java.util.List;
import ru.yandex.praktikum.entity.Task;

public interface HistoryManager {
    void addTask(Task task);

    void remove(long id);

    List<Task> getHistory();
}
