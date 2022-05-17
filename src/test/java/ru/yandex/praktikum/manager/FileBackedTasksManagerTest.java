package ru.yandex.praktikum.manager;

import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.praktikum.entity.*;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.utils.Managers;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileBackedTasksManagerTest {
    private HistoryManager historyManager;
    private FileBackedTasksManager manager;
    private File file;
    private Epic epic;
    private Task task;
    private SubTask subTask;
    private Map<Long, Epic> epics;
    private Map<Long, Task> tasks;
    private Map<Long, SubTask> subtasks;

    @BeforeEach
    void createNewInstance() {
        file = new File("src/test/resources/test.csv");
        manager = new FileBackedTasksManager(file);
        historyManager = Managers.getDefaultHistory();
        epic = new Epic(TypesTasks.EPIC, "First epic", "Clean the room", Status.NEW);
        task = new Task(TypesTasks.TASK, "First task", "Prepare food", Status.NEW);
        subTask = new SubTask(TypesTasks.SUBTASK, "First subtask", "Wash the floors", Status.NEW, epic.getId());
    }

    @AfterEach
    void clearStateManager() {
        file.delete();
        manager = null;
        historyManager = null;
    }

    @Test
    @DisplayName("Create new instance epic")
    void addEpic() {
        epic = manager.addEpic(epic);
        epics = manager.getEpics();
        Epic actual = epics.get(epic.getId());

        assertEquals(epic, actual);
    }

    @Test
    @DisplayName("Create new instance task")
    void addTask() {
        task = manager.addTask(task);
        tasks = manager.getTasks();
        Task actual = tasks.get(task.getId());

        assertEquals(task, actual);
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    @DisplayName("Create new instance subtask")
    void addSubTask(Status status) {
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask.setStatus(status);
        subTask = manager.addSubTask(subTask);
        subtasks = manager.getSubtasks();
        SubTask actual = subtasks.get(subTask.getId());

        assertEquals(subTask, actual);
    }

    @Test
    @DisplayName("Update epic from epic hashmap")
    void updateEpic() {
        epics = manager.getEpics();
        epics.put(epic.getId(), epic);
        epic = new Epic(TypesTasks.EPIC, "Second epic", "Car repair", Status.NEW);
        epic = manager.updateEpic(epic);
        Epic actual = epics.get(epic.getId());

        assertEquals(epic, actual);
    }

    @Test
    @DisplayName("Update task from task hashmap")
    void updateTask() {
        tasks = manager.getTasks();
        tasks.put(task.getId(), task);
        task = new Task(TypesTasks.TASK, "Second task", "Read book", Status.NEW);
        task = manager.updateTask(task);
        Task actual = tasks.get(task.getId());

        assertEquals(task, actual);
    }

    @Test
    @DisplayName("Update subtask from subtask hashmap")
    void updateSubTask() {
        epic = manager.addEpic(epic);
        subtasks = manager.getSubtasks();
        subtasks.put(subTask.getId(), subTask);
        subTask = new SubTask(TypesTasks.SUBTASK, "Second subtask", "Read from 1 to 20 pages", Status.NEW, epic.getId());
        subTask = manager.updateSubTask(subTask);
        SubTask actual = subtasks.get(subTask.getId());

        assertEquals(subTask, actual);
    }

    @Test
    @DisplayName("Get epic by id")
    void getEpicById() {
        epic = manager.addEpic(epic);
        Epic actual = manager.getEpicById(epic.getId());

        assertEquals(epic, actual);
    }

    @Test
    @DisplayName("Get task by id")
    void getTaskById() {
        task = manager.addTask(task);
        Task actual = manager.getTaskById(task.getId());

        assertEquals(task, actual);
    }

    @Test
    @DisplayName("Get subtask by id")
    void getSubTaskById() {
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);
        SubTask actual = manager.getSubTaskById(subTask.getId());

        assertEquals(subTask, actual);
    }

    @Test
    @DisplayName("Delete epic by id")
    void deleteEpicById() {
        epic = manager.addEpic(epic);
        manager.deleteEpicById(epic.getId());
        epics = manager.getEpics();
        int actual = epics.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Delete task by id")
    void deleteTaskById() {
        task = manager.addTask(task);
        manager.deleteTaskById(task.getId());
        tasks = manager.getTasks();
        int actual = tasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Delete subtask by id")
    void deleteSubTaskById() {
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);
        manager.deleteSubTaskById(subTask.getId());
        subtasks = manager.getSubtasks();
        int actual = subtasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Clear epic hashmap")
    void deleteAllEpic() {
        epic = manager.addEpic(epic);
        manager.deleteAllEpic();
        epics = manager.getEpics();
        int actual = epics.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Clear task hashmap")
    void deleteAllTask() {
        task = manager.addTask(task);
        manager.deleteAllTask();
        tasks = manager.getTasks();
        int actual = tasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Clear subtask hashmap")
    void deleteAllSubTask() {
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);
        manager.deleteAllSubTask(epic);
        subtasks = manager.getSubtasks();
        int actual = subtasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Get history call methods")
    void getHistory() {
        historyManager = Mockito.mock(InMemoryHistoryManager.class);
        Mockito.when(historyManager.getHistory()).thenReturn(List.of(task));
        manager.setHistoryManager(historyManager);
        List<Task> actual = manager.getHistory();

        assertEquals(List.of(task), actual);
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
        manager.addEpic(epic);
        manager.addTask(task);
        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).contains(TypesTasks.TASK.name()));
        assertTrue(csv.get(2).contains(TypesTasks.EPIC.name()));
        assertNotNull(csv.get(4));
    }

    @Test
    @DisplayName("Load tasks in file test.csv")
    void load() throws IOException {
        manager.addEpic(epic);
        manager.addTask(task);
        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        manager = new FileBackedTasksManager(file);
        FileBackedTasksManager.loadFromFile(file);
        List<String> csv = Files.readAllLines(file.toPath());

        assertTrue(csv.get(1).contains(TypesTasks.TASK.name()));
        assertTrue(csv.get(2).contains(TypesTasks.EPIC.name()));
        assertNotNull(csv.get(4));

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
