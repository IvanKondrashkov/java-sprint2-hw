package ru.yandex.praktikum.manager;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.LocalDateTime;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import ru.yandex.praktikum.entity.*;
import ru.yandex.praktikum.http.HTTPTaskServer;
import ru.yandex.praktikum.http.KVServer;
import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest {
    private KVServer storage;
    private HTTPTaskManager manager;
    private HTTPTaskServer server;
    private HttpClient client;
    private Gson gson;
    private Task task;
    private Epic epic;
    private SubTask subTask;
    private LocalDateTime currentTime = LocalDateTime.now();

    @BeforeEach
    void init() throws IOException {
        storage = new KVServer();
        storage.start();
        manager = (HTTPTaskManager) Managers.getDefault();
        server = new HTTPTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();

        gson = HTTPTaskServer.getGson();
        task = new Task(TypeTask.TASK, "First task", "Prepare food", Status.NEW, 30, currentTime);
        task = manager.addTask(task);
        task = manager.getTaskById(task.getId());
        epic = new Epic(TypeTask.EPIC, "First epic", "Clean the room", Status.NEW, 0, LocalDateTime.now());
        epic = manager.addEpic(epic);
        currentTime = currentTime.plusMinutes(task.getDuration());
        subTask = new SubTask(TypeTask.SUBTASK, "First subtask", "Wash the floors", Status.NEW, 30, currentTime, epic.getId());
        subTask.setEpicId(epic.getId());
        subTask = manager.addSubTask(subTask);
    }

    @AfterEach
    void tearDown() {
        storage.stop();
        server.stop();
    }

    @Test
    @DisplayName("Saving files to the server")
    void save() {
        assertEquals(1, manager.getTasks().size());
        assertEquals(1, manager.getEpics().size());
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    @DisplayName("Downloading files from the server")
    void load() throws IOException {
        server.stop();
        manager = null;

        manager = (HTTPTaskManager) Managers.getDefault();
        server = new HTTPTaskServer(manager);
        server.start();

        assertEquals(1, manager.getTasks().size());
        assertEquals(1, manager.getEpics().size());
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(1, manager.getHistory().size());
    }
}
