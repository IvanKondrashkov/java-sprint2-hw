package ru.yandex.praktikum.entity;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    private static final String MESSAGE_TO_STRING = "0,SUBTASK,First subtask,Wash the floors,NEW,0";
    private Epic epic;
    private SubTask subTask;

    @BeforeEach
    void createNewInstance() {
        epic = new Epic(TypesTasks.EPIC, "First epic", "Clean the room", Status.NEW);
        subTask = new SubTask(TypesTasks.SUBTASK, "First subtask", "Wash the floors", Status.NEW, epic.getId());
    }

    @Test
    @DisplayName("Get epic id from subtask instance")
    void getEpicId() {
        long actual = subTask.getEpicId();

        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Set by random epic id from subtask instance")
    void setEpicId() {
        long id = new Random().nextInt(2001);
        subTask.setEpicId(id);
        long actual = subTask.getEpicId();

        assertEquals(id, actual);
    }

    @Test
    @DisplayName("Check subtask equals")
    void subTaskEquals() {
        SubTask actual = new SubTask(TypesTasks.SUBTASK, "First subtask", "Wash the floors", Status.NEW, epic.getId());

        assertEquals(subTask, actual);
    }

    @Test
    @DisplayName("Check subtask hashCode")
    void subTaskHashCode() {
        SubTask actual = new SubTask(TypesTasks.SUBTASK, "First subtask", "Wash the floors", Status.NEW, epic.getId());

        assertEquals(subTask.hashCode(), actual.hashCode());
    }

    @Test
    @DisplayName("Check subtask toString")
    void subTaskToString() {
        String actual = subTask.toString();

        assertEquals(MESSAGE_TO_STRING, actual);
    }
}
