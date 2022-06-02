package ru.yandex.praktikum.entity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic;
    private SubTask subTask;
    private final LocalDateTime startTime = LocalDateTime.now();

    @BeforeEach
    void init() {
        epic = new Epic(TypeTask.EPIC, "First epic", "Clean the room", Status.NEW, 0, startTime);
        subTask = new SubTask(TypeTask.SUBTASK, "First subtask", "Wash the floors", Status.NEW, 30, startTime, epic.getId());
    }

    @Test
    @DisplayName("Get collection, Set<SubTask> from epic instance")
    void getSubTaskSetTest() {
        Set<SubTask> actual = epic.getSubTaskSet();

        assertNotNull(actual);
    }

    @Test
    @DisplayName("Set collection, Set<SubTask> from epic instance")
    void setSubTaskSetTest() {
        Set<SubTask> subTaskSet = new HashSet<>();
        subTaskSet.add(subTask);
        epic.setSubTaskSet(subTaskSet);
        int actual = epic.getSubTaskSet().size();

        assertEquals(1, actual);
    }

    @Test
    @DisplayName("Check epic equals")
    void epicEquals() {
        Epic actual = new Epic(TypeTask.EPIC, "First epic", "Clean the room", Status.NEW, 0, startTime);

        assertEquals(epic, actual);
    }

    @Test
    @DisplayName("Check epic hashCode")
    void epicHashCode() {
        Epic actual = new Epic(TypeTask.EPIC, "First epic", "Clean the room", Status.NEW, 0, startTime);

        assertEquals(epic.hashCode(), actual.hashCode());
    }

    @Test
    @DisplayName("Check epic toString")
    void epicToString() {
        final String message = "0,EPIC,First epic,Clean the room,NEW,0," + startTime + "," + startTime.plusMinutes(epic.getDuration()) + ",";
        String actual = epic.toString();

        assertEquals(message, actual);
    }
}
