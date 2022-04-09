package ru.yandex.praktikum.manager;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.yandex.praktikum.entity.Epic;
import ru.yandex.praktikum.entity.SubTask;
import ru.yandex.praktikum.entity.Task;
import ru.yandex.praktikum.entity.Status;
import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {
    private Manager manager;
    private Epic epic;
    private Task task;
    private SubTask subTask;
    private Map<Long, Epic> epics;
    private Map<Long, Task> tasks;
    private Map<Long, SubTask> subtasks;

    @BeforeEach
    void createNewInstance() {
        manager = new Manager();
        epic = new Epic("First epic", "Clean the room", Status.NEW);
        task = new Task("First task", "Prepare food", Status.NEW);
        subTask = new SubTask("First subtask", "Wash the floors", Status.NEW, epic.getId());
    }

    @AfterEach
    void clearStateManager() {
        manager = null;
    }

    @Test
    @DisplayName("Get current id")
    void getIdCurrent() {
        long id = Manager.getIdCurrent();

        assertNotNull(id);
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

    @EnumSource(Status.class)
    @ParameterizedTest
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
        epic = new Epic("Second epic", "Car repair", Status.NEW);
        epic = manager.updateEpic(epic);
        Epic actual = epics.get(epic.getId());

        assertEquals(epic, actual);
    }

    @Test
    @DisplayName("Update task from task hashmap")
    void updateTask() {
        tasks = manager.getTasks();
        tasks.put(task.getId(), task);
        task = new Task("Second task", "Read book", Status.NEW);
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
        subTask = new SubTask("Second subtask", "Read from 1 to 20 pages", Status.NEW, epic.getId());
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
    @DisplayName("Get list all epic")
    void getListAllEpic() {
        epic = manager.addEpic(epic);
        List<Epic> actual = manager.getListAllEpic();

        assertEquals(List.of(epic), actual);
    }

    @Test
    @DisplayName("Get list all task")
    void getListAllTask() {
        task = manager.addTask(task);
        List<Task> actual = manager.getListAllTask();

        assertEquals(List.of(task), actual);
    }

    @Test
    @DisplayName("Get list all subtask")
    void getListAllSubTask() {
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);
        List<SubTask> actual = manager.getListAllSubTask();

        assertEquals(List.of(subTask), actual);
    }

    @Test
    @DisplayName("Get list all subtask from epic")
    void getListAllSubTaskFromEpic() {
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);
        Set<SubTask> subTaskSet = epic.getSubTaskSet();
        subTaskSet.add(subTask);
        List<SubTask> actual = manager.getListAllSubTaskFromEpic(epic);

        assertEquals(List.of(subTask), actual);
    }

    @Test
    @DisplayName("Get epic hashmap")
    void getEpicMap() {
        epics = manager.getEpics();
        int actual = epics.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Get task hashmap")
    void getTaskMap() {
        tasks = manager.getTasks();
        int actual = tasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Get subtask hashmap")
    void getSubTaskMap() {
        subtasks = manager.getSubtasks();
        int actual = subtasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Set epic hashmap")
    void setEpicMap() {
        epics = manager.getEpics();
        epic = manager.addEpic(epic);
        epics.put(epic.getId(), epic);
        manager.setEpics(epics);
        Map<Long, Epic> actual = manager.getEpics();

        assertEquals(epics, actual);
    }

    @Test
    @DisplayName("Set task hashmap")
    void setTaskMap() {
        tasks = manager.getTasks();
        task = manager.addTask(task);
        tasks.put(task.getId(), task);
        manager.setTasks(tasks);
        Map<Long, Task> actual = manager.getTasks();

        assertEquals(tasks, actual);
    }

    @Test
    @DisplayName("Set subtask hashmap")
    void setSubTaskMap() {
        subtasks = manager.getSubtasks();
        epic = manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);
        subtasks.put(subTask.getId(), subTask);
        manager.setSubtasks(subtasks);
        Map<Long, SubTask> actual = manager.getSubtasks();

        assertEquals(subtasks, actual);
    }
}
