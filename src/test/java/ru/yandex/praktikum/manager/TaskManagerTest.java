package ru.yandex.praktikum.manager;

import java.util.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.yandex.praktikum.entity.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Epic epic;
    protected Task task;
    protected SubTask subTask;
    protected LocalDateTime currentTime;
    protected Map<Long, Epic> epics;
    protected Map<Long, Task> tasks;
    protected Map<Long, SubTask> subtasks;

    @BeforeEach
    void init() {
        currentTime = LocalDateTime.now();
        epic = new Epic(TypeTask.EPIC, "First epic", "Clean the room", Status.NEW, 0, LocalDateTime.now());
        epic = manager.addEpic(epic);
        task = new Task(TypeTask.TASK, "First task", "Prepare food", Status.NEW, 30, currentTime);
        task = manager.addTask(task);
        currentTime = currentTime.plusMinutes(task.getDuration()).plusMinutes(10);
        subTask = new SubTask(TypeTask.SUBTASK, "First subtask", "Wash the floors", Status.NEW, 30, currentTime, epic.getId());
        subTask = manager.addSubTask(subTask);
        currentTime = currentTime.plusMinutes(subTask.getDuration());
    }

    @AfterEach
    void tearDown() {
        manager = null;
    }

    @Test
    @DisplayName("Get current id")
    void getIdCurrent() {
        long id = epic.getId();

        assertNotNull(id);
    }

    @Test
    @DisplayName("Create new instance epic")
    void addEpic() {
        epics = manager.getEpics();
        Epic actual = epics.get(epic.getId());

        assertEquals(epic, actual);
    }

    @Test
    @DisplayName("Create new instance task")
    void addTask() {
        tasks = manager.getTasks();
        Task actual = tasks.get(task.getId());

        assertEquals(task, actual);
    }

    @Test
    @DisplayName("Create new instance subtask")
    void addSubTask() {
        subtasks = manager.getSubtasks();
        SubTask actual = subtasks.get(subTask.getId());

        assertEquals(subTask, actual);
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    @DisplayName("Update status by epic, all match condition")
    void updateStatusByEpicAllMatchCondition(Status status) {
        subTask.setStatus(status);
        SubTask subTask1 = new SubTask(TypeTask.SUBTASK, "Second subtask", "Wash the window", status, 25, currentTime, epic.getId());
        subTask1 = manager.addSubTask(subTask1);
        currentTime = currentTime.plusMinutes(subTask1.getDuration());
        Status actual = epic.getStatus();

        assertEquals(status, actual);
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"IN_PROGRESS", "DONE"})
    @DisplayName("Update status by epic, different condition")
    void updateStatusByEpicDifferentCondition(Status status) {
        SubTask subTask1 = new SubTask(TypeTask.SUBTASK, "Second subtask", "Wash the window", status, 25, currentTime, epic.getId());
        subTask1 = manager.addSubTask(subTask1);
        currentTime = currentTime.plusMinutes(subTask1.getDuration());
        Status expected = Status.IN_PROGRESS;
        Status actual = epic.getStatus();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update status by epic is empty subtask set")
    void updateStatusByEpicIsEmptySubtaskSet() {
        epic.setSubTaskSet(Set.of());
        Status expected = Status.NEW;
        Status actual = epic.getStatus();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("If epic from subtask")
    void ifEpicFromSubtask() {
        long expected = epic.getId();
        long actual = subTask.getEpicId();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update epic from epic hashmap")
    void updateEpic() {
        epics = manager.getEpics();
        epics.put(epic.getId(), epic);
        epic = new Epic(epic.getId(), TypeTask.EPIC, "Second epic", "Car repair", Status.NEW, 0, LocalDateTime.now());
        epic = manager.updateEpic(epic);
        Epic actual = epics.get(epic.getId());

        assertEquals(epic, actual);
    }

    @Test
    @DisplayName("Update task from task hashmap")
    void updateTask() {
        tasks = manager.getTasks();
        tasks.put(task.getId(), task);
        task = new Task(task.getId(), TypeTask.TASK, "Second task", "Read book", Status.NEW, 30, LocalDateTime.now());
        task = manager.updateTask(task);
        Task actual = tasks.get(task.getId());

        assertEquals(task, actual);
    }

    @Test
    @DisplayName("Update subtask from subtask hashmap")
    void updateSubTask() {
        subtasks = manager.getSubtasks();
        subtasks.put(subTask.getId(), subTask);
        subTask = new SubTask(subTask.getId(), TypeTask.SUBTASK, "Second subtask", "Read from 1 to 20 pages", Status.NEW, 30, LocalDateTime.now(), epic.getId());
        subTask = manager.updateSubTask(subTask);
        SubTask actual = subtasks.get(subTask.getId());

        assertEquals(subTask, actual);
    }

    @Test
    @DisplayName("Get epic by id")
    void getEpicById() {
        Epic actual = manager.getEpicById(epic.getId());

        assertEquals(epic, actual);
    }

    @Test
    @DisplayName("Get epic by random id")
    void getEpicByRandomId() {
        Epic actual = manager.getEpicById(new Random().nextLong());

        assertNull(actual);
    }

    @Test
    @DisplayName("Get task by id")
    void getTaskById() {
        Task actual = manager.getTaskById(task.getId());

        assertEquals(task, actual);
    }

    @Test
    @DisplayName("Get task by random id")
    void getTaskByRandomId() {
        Task actual = manager.getTaskById(new Random().nextLong());

        assertNull(actual);
    }

    @Test
    @DisplayName("Get subtask by id")
    void getSubTaskById() {
        SubTask actual = manager.getSubTaskById(subTask.getId());

        assertEquals(subTask, actual);
    }

    @Test
    @DisplayName("Get subtask by random id")
    void getSubTaskByRandomId() {
        SubTask actual = manager.getSubTaskById(new Random().nextLong());

        assertNull(actual);
    }

    @Test
    @DisplayName("Delete epic by id")
    void deleteEpicById() {
        manager.deleteEpicById(epic.getId());
        epics = manager.getEpics();
        int actual = epics.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Delete epic by random id")
    void deleteEpicByRandomId() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            manager.deleteEpicById(new Random().nextLong());
        });
        epics = manager.getEpics();
        int actual = epics.size();

        assertNull(exception.getMessage());
        assertEquals(1, actual);
    }

    @Test
    @DisplayName("Delete task by id")
    void deleteTaskById() {
        manager.deleteTaskById(task.getId());
        tasks = manager.getTasks();
        int actual = tasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Delete task by random id")
    void deleteTaskByRandomId() {
        manager.deleteTaskById(new Random().nextLong());
        tasks = manager.getTasks();
        int actual = tasks.size();

        assertEquals(1, actual);
    }

    @Test
    @DisplayName("Delete subtask by id")
    void deleteSubTaskById() {
        manager.deleteSubTaskById(subTask.getId());
        subtasks = manager.getSubtasks();
        int actual = subtasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Delete subtask by random id")
    void deleteSubTaskByRandomId() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            manager.deleteSubTaskById(new Random().nextLong());
        });
        subtasks = manager.getSubtasks();
        int actual = subtasks.size();

        assertNull(exception.getMessage());
        assertEquals(1, actual);
    }

    @Test
    @DisplayName("Clear epic hashmap")
    void deleteAllEpic() {
        manager.deleteAllEpic();
        epics = manager.getEpics();
        int actual = epics.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Clear task hashmap")
    void deleteAllTask() {
        manager.deleteAllTask();
        tasks = manager.getTasks();
        int actual = tasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Clear subtask hashmap")
    void deleteAllSubTask() {
        manager.deleteAllSubTask(epic);
        subtasks = manager.getSubtasks();
        int actual = subtasks.size();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Get list all epic")
    void getListAllEpic() {
        List<Epic> actual = manager.getListAllEpic();

        assertEquals(List.of(epic), actual);
    }

    @Test
    @DisplayName("Get list all task")
    void getListAllTask() {
        List<Task> actual = manager.getListAllTask();

        assertEquals(List.of(task), actual);
    }

    @Test
    @DisplayName("Get list all subtask")
    void getListAllSubTask() {
        List<SubTask> actual = manager.getListAllSubTask();

        assertEquals(List.of(subTask), actual);
    }

    @Test
    @DisplayName("Get list all subtask from epic")
    void getListAllSubTaskFromEpic() {
        List<SubTask> actual = manager.getListAllSubTaskFromEpic(epic);

        assertEquals(List.of(subTask), actual);
    }

    @Test
    @DisplayName("Get list tasks prioritized")
    void getPrioritizedTasks() {
        List<Task> priority = manager.getPrioritizedTasks();
        task = priority.get(0);
        subTask = (SubTask) priority.get(1);

        assertTrue(task.getEndTime().isBefore(subTask.getStartTime()));
    }

    @Test
    @DisplayName("Get epic hashmap")
    void getEpicMap() {
        epics = manager.getEpics();
        int actual = epics.size();

        assertEquals(1, actual);
    }

    @Test
    @DisplayName("Get task hashmap")
    void getTaskMap() {
        tasks = manager.getTasks();
        int actual = tasks.size();

        assertEquals(1, actual);
    }

    @Test
    @DisplayName("Get subtask hashmap")
    void getSubTaskMap() {
        subtasks = manager.getSubtasks();
        int actual = subtasks.size();

        assertEquals(1, actual);
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
        currentTime = currentTime.plusMinutes(task.getDuration());
        task.setStartTime(currentTime);
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
        currentTime = currentTime.plusMinutes(subTask.getDuration());
        subTask.setStartTime(currentTime);
        subTask = manager.addSubTask(subTask);
        subtasks.put(subTask.getId(), subTask);
        manager.setSubtasks(subtasks);
        Map<Long, SubTask> actual = manager.getSubtasks();

        assertEquals(subtasks, actual);
    }
}
