package ru.yandex.praktikum.manager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    void init() {
        manager = (InMemoryTaskManager) Managers.getDefault();
        super.init();
    }
}
