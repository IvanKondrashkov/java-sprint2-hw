package ru.yandex.praktikum.manager;

import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import ru.yandex.praktikum.entity.*;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.utils.Managers;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file;

    @BeforeEach
    @Override
    void init() {
        file = new File("src/test/resources/test.csv");
        manager = (FileBackedTasksManager) Managers.getFileBackedTasksManager(file);
        super.init();
    }

    @AfterEach
    void tearDown() {
        file.delete();
    }

    @Test
    @DisplayName("Get file by manager")
    void getFile() {
        File actual = manager.getFile();

        assertEquals(file, actual);
    }

    @Test
    @DisplayName("Save tasks in file test.csv")
    void save() throws IOException {
        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        manager.getSubTaskById(subTask.getId());
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).contains(TypeTask.TASK.name()));
        assertTrue(csv.get(2).contains(TypeTask.EPIC.name()));
        assertTrue(csv.get(3).contains(TypeTask.SUBTASK.name()));
        assertTrue(csv.get(4).isEmpty());
        assertTrue(csv.get(5).contains(String.valueOf(task.getId())));
        assertEquals(6, csv.size());
    }

    @Test
    @DisplayName("Save tasks in file test.csv, epic without subtasks")
    void saveEpicWithoutSubtasks() throws IOException {
        manager.deleteSubTaskById(subTask.getId());
        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).contains(TypeTask.TASK.name()));
        assertTrue(csv.get(2).contains(TypeTask.EPIC.name()));
        assertTrue(csv.get(3).isEmpty());
        assertTrue(csv.get(4).contains(String.valueOf(task.getId())));
        assertEquals(5, csv.size());
    }

    @Test
    @DisplayName("Save tasks in file test.csv, is empty list tasks")
    void saveIsEmptyListTasks() throws IOException {
        manager.deleteTaskById(task.getId());
        manager.deleteSubTaskById(subTask.getId());
        manager.deleteEpicById(epic.getId());
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).isEmpty());
        assertEquals(2, csv.size());
    }

    @Test
    @DisplayName("Save tasks in file test.csv, is empty list history")
    void saveIsEmptyListHistory() throws IOException {
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).contains(TypeTask.TASK.name()));
        assertTrue(csv.get(2).contains(TypeTask.EPIC.name()));
        assertTrue(csv.get(3).contains(TypeTask.SUBTASK.name()));
        assertTrue(csv.get(4).isEmpty());
        assertEquals(5, csv.size());
    }

    @Test
    @DisplayName("Load tasks in file test.csv")
    void load() throws IOException {
        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        manager.getSubTaskById(subTask.getId());
        manager = new FileBackedTasksManager(file);
        FileBackedTasksManager.loadFromFile(file);
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).contains(TypeTask.TASK.name()));
        assertTrue(csv.get(2).contains(TypeTask.EPIC.name()));
        assertTrue(csv.get(3).contains(TypeTask.SUBTASK.name()));
        assertTrue(csv.get(4).isEmpty());
        assertTrue(csv.get(5).contains(String.valueOf(task.getId())));
        assertEquals(6, csv.size());

    }

    @Test
    @DisplayName("Load tasks in file test.csv, epic without subtasks")
    void loadEpicWithoutSubtasks() throws IOException {
        manager.deleteSubTaskById(subTask.getId());
        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        manager = new FileBackedTasksManager(file);
        FileBackedTasksManager.loadFromFile(file);
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).contains(TypeTask.TASK.name()));
        assertTrue(csv.get(2).contains(TypeTask.EPIC.name()));
        assertTrue(csv.get(3).isEmpty());
        assertTrue(csv.get(4).contains(String.valueOf(task.getId())));
        assertEquals(5, csv.size());
    }

    @Test
    @DisplayName("Load tasks in file test.csv, is empty list tasks")
    void loadIsEmptyListTasks() throws IOException {
        manager.deleteTaskById(task.getId());
        manager.deleteSubTaskById(subTask.getId());
        manager.deleteEpicById(epic.getId());
        manager = new FileBackedTasksManager(file);
        FileBackedTasksManager.loadFromFile(file);
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).isEmpty());
        assertEquals(2, csv.size());
    }

    @Test
    @DisplayName("Load tasks in file test.csv, is empty list history")
    void loadIsEmptyListHistory() throws IOException {
        manager = new FileBackedTasksManager(file);
        FileBackedTasksManager.loadFromFile(file);
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).contains(TypeTask.TASK.name()));
        assertTrue(csv.get(2).contains(TypeTask.EPIC.name()));
        assertTrue(csv.get(3).contains(TypeTask.SUBTASK.name()));
        assertTrue(csv.get(4).isEmpty());
        assertEquals(5, csv.size());

    }

    @Test
    @DisplayName("Checking for throwing an exception when saving to a file test.csv")
    void checkExceptionFromSave() {
        Exception exception = assertThrows(ManagerSaveException.class, () -> {
            file = new File("/");
            file.setReadOnly();
            manager = new FileBackedTasksManager(file);
            manager.addTask(task);
        });

        String message = "Saving to a file ended incorrectly!";
        String actual = exception.getMessage();

        assertEquals(message, actual);
    }

    @Test
    @DisplayName("Checking for throwing an exception when loading from a file test.csv")
    void checkExceptionFromLoad() {
        Exception exception = assertThrows(ManagerSaveException.class, () -> {
            file = new File("/");
            file.setReadable(false, false);
            manager = new FileBackedTasksManager(file);
            FileBackedTasksManager.loadFromFile(file);
        });

        String message = "The download from the file ended incorrectly!";
        String actual = exception.getMessage();

        assertEquals(message, actual);
    }
}
