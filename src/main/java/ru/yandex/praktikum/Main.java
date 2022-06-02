package ru.yandex.praktikum;

import java.io.File;
import java.time.LocalDateTime;
import ru.yandex.praktikum.entity.*;
import ru.yandex.praktikum.utils.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.manager.FileBackedTasksManager;

public class Main {
    private static final File FILE = new File("src/main/resources/data.csv");
    private static FileBackedTasksManager fileManager = (FileBackedTasksManager) Managers.getFileBackedTasksManager(FILE);
    private static LocalDateTime currentTime = LocalDateTime.now();


    public static void main(String[] args) {
        Task task1 = new Task(TypeTask.TASK, "#1", "First task", Status.NEW, 30, currentTime);
        currentTime = currentTime.plusMinutes(task1.getDuration());
        task1 = fileManager.addTask(task1);

        Task task2 = new Task(TypeTask.TASK, "#2", "Second task", Status.NEW, 25, currentTime);
        currentTime = currentTime.plusMinutes(task2.getDuration());
        task2 = fileManager.addTask(task2);

        Task task3 = new Task(TypeTask.TASK, "#3", "Third task", Status.NEW, 25, null);
        task3 = fileManager.addTask(task3);

        Epic epic1 = new Epic(TypeTask.EPIC, "#1", "First epic", Status.NEW, 0, LocalDateTime.now());
        epic1 = fileManager.addEpic(epic1);

        SubTask subTask1 = new SubTask(TypeTask.SUBTASK, "#1", "First subtask!", Status.NEW, 40, currentTime, epic1.getId());
        currentTime = currentTime.plusMinutes(subTask1.getDuration());
        subTask1 = fileManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask(TypeTask.SUBTASK, "#2", "Second subtask!", Status.NEW, 20, currentTime, epic1.getId());
        currentTime = currentTime.plusMinutes(subTask2.getDuration());
        subTask2 = fileManager.addSubTask(subTask2);

        SubTask subTask3 = new SubTask(TypeTask.SUBTASK, "#3", "Third subtask!", Status.NEW, 50, currentTime, epic1.getId());
        subTask3 = fileManager.addSubTask(subTask3);

        Epic epic2 = new Epic(TypeTask.EPIC, "#2", "Second epic", Status.NEW, 0, LocalDateTime.now());
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
        printGetPrioritizedTasks();

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
        File file = fileManager.getFile();

        fileManager = null;
        fileManager = FileBackedTasksManager.loadFromFile(file);
        fileManager.addEpic(epic2);
        fileManager.getEpicById(epic2.getId());
        fileManager.getTaskById(task3.getId());
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

    public static void printGetPrioritizedTasks() {
        System.out.println("method call printGetPrioritizedTasks!");
        fileManager.getPrioritizedTasks().forEach(System.out::println);
    }
}
