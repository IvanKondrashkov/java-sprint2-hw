package ru.yandex.praktikum.manager;

import java.util.List;
import java.util.LinkedList;
import java.util.Objects;
import ru.yandex.praktikum.entity.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new LinkedList<>();

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)) {
            return;
        }
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
