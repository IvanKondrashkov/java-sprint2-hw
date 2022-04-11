package ru.yandex.praktikum.manager;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import ru.yandex.praktikum.entity.Task;
import ru.yandex.praktikum.entity.Status;
import ru.yandex.praktikum.utils.Managers;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static HistoryManager historyManager;

    @ParameterizedTest
    @MethodSource("provideArguments")
    @DisplayName("Add task to history")
    void addTask(List<Task> expected) {
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected.size(), actual.size());
    }

    static Stream<Arguments> provideArguments() {
        return Stream.of(
                Arguments.of(fillListTasks(11))
        );
    }

    static List<Task> fillListTasks(int size) {
        historyManager = Managers.getDefaultHistory();
        Task task = new Task("First task", "Prepare food", Status.NEW);

        for (int i = 0; i < size; i++) {
            historyManager.addTask(task);
        }

        return historyManager.getHistory();
    }
}
