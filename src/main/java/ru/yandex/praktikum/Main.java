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
        Task task1 = new Task("#1", "First task", Status.NEW);
        task1 = manager.addTask(task1);

        Task task2 = new Task("#2", "Second task", Status.NEW);
        task2 = manager.addTask(task2);

        Epic epic1 = new Epic("#1", "First epic", Status.NEW);
        epic1 = manager.addEpic(epic1);

        SubTask subTask1 = new SubTask("#1", "First subtask!", Status.NEW, epic1.getId());
        subTask1 = manager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("#2", "Second subtask!", Status.NEW, epic1.getId());
        subTask2 = manager.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("#3", "Third subtask!", Status.NEW, epic1.getId());
        subTask3 = manager.addSubTask(subTask3);

        Epic epic2 = new Epic("#2", "Second epic", Status.NEW);
        epic2 = manager.addEpic(epic2);

        manager.getTaskById(task1.getId());
        manager.getSubTaskById(subTask1.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(subTask3.getId());
        manager.getEpicById(epic1.getId());

        task2.setStatus(Status.IN_PROGRESS);
        manager.getTaskById(task2.getId());
        subTask1.setStatus(Status.IN_PROGRESS);
        manager.getSubTaskById(subTask1.getId());
        subTask2.setStatus(Status.IN_PROGRESS);
        manager.getSubTaskById(subTask2.getId());
        manager.updateEpic(epic1);
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        printHistory();

        subTask1.setStatus(Status.DONE);
        manager.getSubTaskById(subTask1.getId());
        subTask2.setStatus(Status.DONE);
        manager.getSubTaskById(subTask2.getId());
        subTask3.setStatus(Status.DONE);
        manager.getSubTaskById(subTask3.getId());
        manager.updateEpic(epic1);
        manager.getEpicById(epic1.getId());
        printHistory();

        manager.deleteTaskById(task1.getId());
        manager.deleteEpicById(epic1.getId());
        manager.deleteEpicById(epic2.getId());
        printHistory();
        printTask();
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
