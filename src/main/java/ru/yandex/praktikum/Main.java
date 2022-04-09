package ru.yandex.praktikum;

import java.util.Map;
import ru.yandex.praktikum.entity.Epic;
import ru.yandex.praktikum.entity.Task;
import ru.yandex.praktikum.entity.SubTask;
import ru.yandex.praktikum.manager.Manager;
import ru.yandex.praktikum.entity.Status;

public class Main {
    private static Manager manager = new Manager();
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

        subTask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask1);
        printTask();

        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);
        subTask2.setStatus(Status.DONE);
        manager.updateSubTask(subTask2);
        printTask();

        manager.deleteSubTaskById(subTask1.getId());
        printTask();

        manager.deleteAllSubTask(epic);
        printTask();
        manager.deleteEpicById(epic.getId());
        printTask();
    }

    public static void printTask() {
        subtasks.values().forEach(System.out::println);
        epics.values().forEach(System.out::println);
        System.out.println();
    }
}
