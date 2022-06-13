package ru.yandex.praktikum.manager;

import java.util.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import ru.yandex.praktikum.entity.*;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private TaskManager manager;
    private Epic epic;
    private Task task;
    private SubTask subTask;
    private LocalDateTime currentTime;

    @BeforeEach
    void init() {
        historyManager = Managers.getDefaultHistory();
        manager = Managers.getDefault();
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
        historyManager = null;
        manager = null;
    }

    @Test
    @DisplayName("Add task to history")
    void addTask() {
        historyManager.addTask(epic);
        historyManager.addTask(task);
        historyManager.addTask(subTask);
        List<Task> actual = historyManager.getHistory();

        assertEquals(3, actual.size());
    }

    @Test
    @DisplayName("Add duplicate task to history")
    void addTaskDuplicate() {
        historyManager.addTask(task);
        historyManager.addTask(task);
        List<Task> actual = historyManager.getHistory();

        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("Is empty history list")
    void isEmptyHistoryList() {
        List<Task> actual = historyManager.getHistory();

        assertEquals(0, actual.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    @DisplayName("Remove task from history by index")
    void removeTaskByIndex(long index) {
        historyManager.addTask(epic);
        historyManager.addTask(task);
        historyManager.addTask(subTask);
        historyManager.remove(index);
        List<Task> tasks = historyManager.getHistory();

        assertEquals(-1, tasks.indexOf(index));
    }
}
