package ru.yandex.praktikum.entity;

import java.util.EnumSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class TypeTaskTest {
    @ParameterizedTest
    @EnumSource(names = {"EPIC","TASK","SUBTASK"})
    @DisplayName("Checking enumeration instances")
    void enumSourceFromTypesTasks(TypeTask type) {
        assertTrue(EnumSet.of(TypeTask.EPIC, TypeTask.TASK, TypeTask.SUBTASK).contains(type));
    }
}
