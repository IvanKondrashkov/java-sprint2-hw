package ru.yandex.praktikum.entity;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static final String MESSAGE_TO_STRING = "0,TASK,First task,Prepare food,NEW,";
    private Task task;

    @BeforeEach
    void createNewInstance() {
        task = new Task(TypesTasks.TASK, "First task", "Prepare food", Status.NEW);
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
        TypesTasks actual = task.getType();

        assertEquals(TypesTasks.TASK, actual);
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
        TypesTasks actual = task.getType();

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
        Task actual = new Task(TypesTasks.TASK, "First task", "Prepare food", Status.NEW);

        assertEquals(task, actual);
    }

    @Test
    @DisplayName("Check task hashCode")
    void taskHashCode() {
        Task actual = new Task(TypesTasks.TASK, "First task", "Prepare food", Status.NEW);

        assertEquals(task.hashCode(), actual.hashCode());
    }

    @Test
    @DisplayName("Check task toString")
    void taskToString() {
        String actual = task.toString();

        assertEquals(MESSAGE_TO_STRING, actual);
    }
}
