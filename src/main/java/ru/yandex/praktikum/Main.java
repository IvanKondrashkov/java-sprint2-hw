package ru.yandex.praktikum;

import java.io.IOException;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.http.HTTPTaskServer;
import ru.yandex.praktikum.manager.TaskManager;

public class Main {
    private static final TaskManager manager = Managers.getDefault();

    public static void main(String[] args) throws IOException {
        HTTPTaskServer server = new HTTPTaskServer(manager);
        server.start();
    }
}
