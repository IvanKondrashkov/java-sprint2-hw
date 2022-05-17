package ru.yandex.praktikum.entity;

import java.util.EnumSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class StatusTest {
    @ParameterizedTest
    @EnumSource(names = {"NEW","IN_PROGRESS","DONE"})
    @DisplayName("Checking enumeration instances")
    void enumSourceFromStatus(Status status) {
        assertTrue(EnumSet.of(Status.NEW, Status.IN_PROGRESS, Status.DONE).contains(status));
    }
}
