package ru.yandex.praktikum;

import java.util.Map;
import ru.yandex.praktikum.entity.Task;
import ru.yandex.praktikum.entity.Epic;
import ru.yandex.praktikum.entity.SubTask;
import ru.yandex.praktikum.entity.Status;
import ru.yandex.praktikum.utils.Managers;
import ru.yandex.praktikum.manager.InMemoryTaskManager;

public class Main {
    private static InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();
    private static Map<Long, Task> tasks = manager.getTasks();
    private static Map<Long, Epic> epics = manager.getEpics();
    private static Map<Long, SubTask> subtasks = manager.getSubtasks();

    public static void main(String[] args) {
        Task task = new Task("#1", "First task", Status.NEW);
        task = manager.addTask(task);

        Epic epic = new Epic("#1", "First epic", Status.NEW);
        epic = manager.addEpic(epic);

        SubTask subTask1 = new SubTask("#1", "First subtask!", Status.NEW, epic.getId());
        subTask1 = manager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("#2", "Second subtask!", Status.NEW, epic.getId());
        subTask2 = manager.addSubTask(subTask2);

        manager.getTaskById(task.getId());
        manager.getSubTaskById(subTask1.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getEpicById(epic.getId());

        task.setStatus(Status.IN_PROGRESS);
        manager.getTaskById(task.getId());
        subTask1.setStatus(Status.IN_PROGRESS);
        manager.getSubTaskById(subTask1.getId());
        subTask2.setStatus(Status.IN_PROGRESS);
        manager.getSubTaskById(subTask2.getId());
        manager.updateEpic(epic);
        manager.getEpicById(epic.getId());

        subTask1.setStatus(Status.DONE);
        manager.getSubTaskById(subTask1.getId());
        subTask2.setStatus(Status.DONE);
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(task.getId());
        manager.updateEpic(epic);
        printTask();
        printHistory();
    }

    public static void printTask() {
        System.out.println("method call printTask!");
        tasks.values().forEach(System.out::println);
        epics.values().forEach(System.out::println);
        subtasks.values().forEach(System.out::println);
    }

    public static void printHistory() {
        System.out.println("method call printHistory!");
        manager.getHistory().forEach(System.out::println);
    }
}
