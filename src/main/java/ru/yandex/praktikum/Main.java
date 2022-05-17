package ru.yandex.praktikum;

import java.io.File;
import ru.yandex.praktikum.entity.*;
import ru.yandex.praktikum.utils.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.manager.FileBackedTasksManager;

public class Main {
    private static FileBackedTasksManager fileManager = (FileBackedTasksManager) Managers.getFileBackedTasksManager();
    private static final File file = fileManager.getFile();

    public static void main(String[] args) {
        Task task1 = new Task(TypesTasks.TASK, "#1", "First task", Status.NEW);
        task1 = fileManager.addTask(task1);
        Task task2 = new Task(TypesTasks.TASK, "#2", "Second task", Status.NEW);
        task2 = fileManager.addTask(task2);

        Epic epic1 = new Epic(TypesTasks.EPIC, "#1", "First epic", Status.NEW);
        epic1 = fileManager.addEpic(epic1);

        SubTask subTask1 = new SubTask(TypesTasks.SUBTASK, "#1", "First subtask!", Status.NEW, epic1.getId());
        subTask1 = fileManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask(TypesTasks.SUBTASK, "#2", "Second subtask!", Status.NEW, epic1.getId());
        subTask2 = fileManager.addSubTask(subTask2);

        SubTask subTask3 = new SubTask(TypesTasks.SUBTASK, "#3", "Third subtask!", Status.NEW, epic1.getId());
        subTask3 = fileManager.addSubTask(subTask3);

        Epic epic2 = new Epic(TypesTasks.EPIC, "#2", "Second epic", Status.NEW);
        epic2 = fileManager.addEpic(epic2);

        fileManager.getTaskById(task1.getId());
        fileManager.getSubTaskById(subTask1.getId());
        fileManager.getSubTaskById(subTask2.getId());
        fileManager.getSubTaskById(subTask3.getId());
        fileManager.getEpicById(epic1.getId());

        task2.setStatus(Status.IN_PROGRESS);
        fileManager.getTaskById(task2.getId());
        subTask1.setStatus(Status.IN_PROGRESS);
        fileManager.getSubTaskById(subTask1.getId());
        subTask2.setStatus(Status.IN_PROGRESS);
        fileManager.getSubTaskById(subTask2.getId());
        fileManager.updateEpic(epic1);
        fileManager.getEpicById(epic1.getId());
        fileManager.getEpicById(epic2.getId());
        printHistory();

        subTask1.setStatus(Status.DONE);
        fileManager.getSubTaskById(subTask1.getId());
        subTask2.setStatus(Status.DONE);
        fileManager.getSubTaskById(subTask2.getId());
        subTask3.setStatus(Status.DONE);
        fileManager.getSubTaskById(subTask3.getId());
        fileManager.updateEpic(epic1);
        fileManager.getEpicById(epic1.getId());
        printHistory();

        fileManager.deleteTaskById(task1.getId());
        fileManager.deleteEpicById(epic1.getId());
        fileManager.deleteEpicById(epic2.getId());
        printHistory();
        printTask(fileManager);

        fileManager = null;
        fileManager = FileBackedTasksManager.loadFromFile(file);
        fileManager.addEpic(epic2);
        fileManager.getEpicById(epic2.getId());
        printHistory();
        printTask(fileManager);
    }

    public static void printTask(TaskManager manager) {
        System.out.println("method call printTask!");
        manager.getListAllTask().forEach(System.out::println);
        manager.getListAllEpic().forEach(System.out::println);
        manager.getListAllSubTask().forEach(System.out::println);
    }

    public static void printHistory() {
        System.out.println("method call printHistory!");
        fileManager.getHistory().forEach(System.out::println);
    }
}
