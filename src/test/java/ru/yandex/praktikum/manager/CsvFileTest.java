package ru.yandex.praktikum.manager;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.yandex.praktikum.entity.*;
import static org.junit.jupiter.api.Assertions.*;

public class CsvFileTest {
    private FileBackedTasksManager manager;
    private Epic epic;
    private Task task;

    @BeforeEach
    void createNewInstance() {
        manager = new FileBackedTasksManager(new File("src/test/resources/data.csv"));
        epic = new Epic(TypesTasks.EPIC, "First epic", "Clean the room", Status.NEW);
        task = new Task(TypesTasks.TASK, "First task", "Prepare food", Status.NEW);
    }

    @AfterEach
    void clearStateManager() {
        manager = null;
    }

    @Test
    void fillFileCsv() {
        manager.addTask(task);
        manager.addEpic(epic);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
    @DisplayName("Read line file data.csv")
    void readFileCsv(Long id, String type, String name, String status, String description, String epicId) {
        assertNotNull(id);
        assertNotNull(type);
        assertNotNull(name);
        assertNotNull(status);
        assertNotNull(description);
        assertNull(epicId);
    }
}
