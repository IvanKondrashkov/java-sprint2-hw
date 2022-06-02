package ru.yandex.praktikum.entity;

import java.time.LocalDateTime;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    private Epic epic;
    private SubTask subTask;
    private final LocalDateTime startTime = LocalDateTime.now();

    @BeforeEach
    void init() {
        epic = new Epic(TypeTask.EPIC, "First epic", "Clean the room", Status.NEW, 0, startTime);
        subTask = new SubTask(TypeTask.SUBTASK, "First subtask", "Wash the floors", Status.NEW, 30, startTime, epic.getId());
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
        SubTask actual = new SubTask(TypeTask.SUBTASK, "First subtask", "Wash the floors", Status.NEW, 30, startTime, epic.getId());

        assertEquals(subTask, actual);
    }

    @Test
    @DisplayName("Check subtask hashCode")
    void subTaskHashCode() {
        SubTask actual = new SubTask(TypeTask.SUBTASK, "First subtask", "Wash the floors", Status.NEW, 30, startTime, epic.getId());

        assertEquals(subTask.hashCode(), actual.hashCode());
    }

    @Test
    @DisplayName("Check subtask toString")
    void subTaskToString() {
        final String message = "0,SUBTASK,First subtask,Wash the floors,NEW,30," + startTime + "," + startTime.plusMinutes(subTask.getDuration()) + "," + subTask.getEpicId();
        String actual = subTask.toString();

        assertEquals(message, actual);
    }
}
