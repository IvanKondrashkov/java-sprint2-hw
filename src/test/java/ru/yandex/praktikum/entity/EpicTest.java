package ru.yandex.praktikum.entity;

import java.util.Set;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private static final String MESSAGE_TO_STRING = "0,EPIC,First epic,Clean the room,NEW,";
    private Epic epic;
    private SubTask subTask;

    @BeforeEach
    void createNewInstance() {
        epic = new Epic(TypesTasks.EPIC, "First epic", "Clean the room", Status.NEW);
        subTask = new SubTask(TypesTasks.SUBTASK, "First subtask", "Wash the floors", Status.NEW, epic.getId());
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
        Epic actual = new Epic(TypesTasks.EPIC, "First epic", "Clean the room", Status.NEW);

        assertEquals(epic, actual);
    }

    @Test
    @DisplayName("Check epic hashCode")
    void epicHashCode() {
        Epic actual = new Epic(TypesTasks.EPIC, "First epic", "Clean the room", Status.NEW);

        assertEquals(epic.hashCode(), actual.hashCode());
    }

    @Test
    @DisplayName("Check epic toString")
    void epicToString() {
        String actual = epic.toString();

        assertEquals(MESSAGE_TO_STRING, actual);
    }
}
