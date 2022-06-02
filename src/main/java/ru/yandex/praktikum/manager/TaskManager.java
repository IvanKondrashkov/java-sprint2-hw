package ru.yandex.praktikum.manager;

import java.util.Map;
import java.util.List;
import ru.yandex.praktikum.entity.Epic;
import ru.yandex.praktikum.entity.SubTask;
import ru.yandex.praktikum.entity.Task;

public interface TaskManager {
    Epic addEpic(Epic epic);

    Task addTask(Task task);

    SubTask addSubTask(SubTask subTask);

    Epic updateEpic(Epic epic);

    Task updateTask(Task task);

    SubTask updateSubTask(SubTask subTask);

    Epic getEpicById(long id);

    Task getTaskById(long id);

    SubTask getSubTaskById(long id);

    void deleteEpicById(long id);

    void deleteTaskById(long id);

    void deleteSubTaskById(long id);

    void deleteAllEpic();

    void deleteAllTask();

    void deleteAllSubTask(Epic epic);

    Map<Long, Epic> getEpics();

    Map<Long, Task> getTasks();

    Map<Long, SubTask> getSubtasks();

    void setEpics(Map<Long, Epic> epics);

    void setTasks(Map<Long, Task> tasks);

    void setSubtasks(Map<Long, SubTask> subtasks);

    List<Epic> getListAllEpic();

    List<Task> getListAllTask();

    List<SubTask> getListAllSubTask();

    List<SubTask> getListAllSubTaskFromEpic(Epic epic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
