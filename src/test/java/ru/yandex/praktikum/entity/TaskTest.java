package ru.yandex.praktikum.entity;

import java.time.LocalDateTime;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;
    private final LocalDateTime startTime = LocalDateTime.now();

    @BeforeEach
    void init() {
        task = new Task(TypeTask.TASK, "First task", "Prepare food", Status.NEW, 30, startTime);
    }

    @Test
    @DisplayName("Get task id from task instance")
    void getId() {
        long actual = task.getId();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Get task type from task instance")
    void getType() {
        TypeTask actual = task.getType();

        assertEquals(TypeTask.TASK, actual);
    }

    @Test
    @DisplayName("Get task name from task instance")
    void getName() {
        String actual = task.getName();

        assertEquals("First task", actual);
    }

    @Test
    @DisplayName("Get task description from task instance")
    void getDescription() {
        String actual = task.getDescription();

        assertEquals("Prepare food", actual);
    }

    @Test
    @DisplayName("Get task status from task instance")
    void getStatus() {
        Status actual = task.getStatus();

        assertEquals(Status.NEW, actual);
    }

    @Test
    @DisplayName("Set by random task id from task instance")
    void setId() {
        long id = new Random().nextInt(2001);
        task.setId(id);
        long actual = task.getId();

        assertEquals(id, actual);
    }

    @Test
    @DisplayName("Set task type from task instance")
    void setType() {
        task.setType(null);
        TypeTask actual = task.getType();

        assertNull(actual);
    }

    @Test
    @DisplayName("Set task name from task instance")
    void setName() {
        String name = "New name task";
        task.setName(name);
        String actual = task.getName();

        assertEquals(name, actual);
    }

    @Test
    @DisplayName("Set task description from task instance")
    void setDescription() {
        String description = "New description task";
        task.setDescription(description);
        String actual = task.getDescription();

        assertEquals(description, actual);
    }

    @Test
    @DisplayName("Set task status from task instance")
    void setStatus() {
        Status status = Status.DONE;
        task.setStatus(status);
        Status actual = task.getStatus();

        assertEquals(status, actual);
    }

    @Test
    @DisplayName("Check task equals")
    void taskEquals() {
        Task actual = new Task(TypeTask.TASK, "First task", "Prepare food", Status.NEW, 30, startTime);

        assertEquals(task, actual);
    }

    @Test
    @DisplayName("Check task hashCode")
    void taskHashCode() {
        Task actual = new Task(TypeTask.TASK, "First task", "Prepare food", Status.NEW, 30, startTime);

        assertEquals(task.hashCode(), actual.hashCode());
    }

    @Test
    @DisplayName("Check task toString")
    void taskToString() {
        final String message = "0,TASK,First task,Prepare food,NEW,30," + startTime + "," + startTime.plusMinutes(task.getDuration()) + ",";
        String actual = task.toString();

        assertEquals(message, actual);
    }
}
