package ru.yandex.praktikum.manager;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.praktikum.entity.Epic;
import ru.yandex.praktikum.entity.SubTask;
import ru.yandex.praktikum.entity.Task;
import ru.yandex.praktikum.entity.Status;
import java.util.concurrent.atomic.AtomicLong;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
public class Manager {
    private static final AtomicLong idCurrent = new AtomicLong();
    private Map<Long, Epic> epics;
    private Map<Long, Task> tasks;
    private Map<Long, SubTask> subtasks;

    public Manager() {
        this.epics = new HashMap<>();
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    public static long getIdCurrent() {
        return idCurrent.incrementAndGet();
    }

    public Epic addEpic(Epic epic) {
        epic.setId(Manager.getIdCurrent());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Task addTask(Task task) {
        task.setId(Manager.getIdCurrent());
        tasks.put(task.getId(), task);
        return task;
    }

    public SubTask addSubTask(SubTask subTask) {
        subTask.setId(Manager.getIdCurrent());
        Epic epic = epics.get(subTask.getEpicId());

        if (epics.containsKey(epic.getId())) {
            Set<SubTask> subTaskSet = epic.getSubTaskSet();
            subTaskSet.add(subTask);
            subtasks.put(subTask.getId(), subTask);
        }
        updateStatus(epic);
        updateEpic(epic);
        return subTask;
    }

    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
        return epic;
    }

    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
        return task;
    }

    public SubTask updateSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());

        if (subtasks.containsKey(subTask.getId())) {
            subtasks.put(subTask.getId(), subTask);
        }
        updateStatus(epic);
        updateEpic(epic);
        return subTask;
    }

    public Epic getEpicById(long id) {
        return epics.get(id);
    }

    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskById(long id) {
        return subtasks.get(id);
    }

    public void deleteEpicById(long id) {
        if (epics.containsKey(id)) {
            epics.remove(id);
        }
    }

    public void deleteTaskById(long id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteSubTaskById(long id) {
        SubTask subTask = subtasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());

        if (subtasks.containsKey(id)) {
            Set<SubTask> subTaskSet = epic.getSubTaskSet();
            subTaskSet = subTaskSet.stream().filter(val -> !val.equals(subTask)).collect(Collectors.toSet());
            epic.setSubTaskSet(subTaskSet);
            subtasks.remove(id);
        }
        updateStatus(epic);
        updateEpic(epic);
    }

    public void deleteAllEpic() {
        if (!epics.isEmpty()) {
            epics.clear();
        }
    }

    public void deleteAllTask() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    public void deleteAllSubTask(Epic epic) {
        Set<SubTask> subTaskSet = epic.getSubTaskSet();

        if (!subtasks.isEmpty()) {
            subTaskSet.clear();
            subtasks.clear();
        }
        updateStatus(epic);
        updateEpic(epic);
    }

    public List<Epic> getListAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public List<Task> getListAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public List<SubTask> getListAllSubTask() {
        return new ArrayList<>(subtasks.values());
    }

    public List<SubTask> getListAllSubTaskFromEpic(Epic epic) {
        return new ArrayList<>(epic.getSubTaskSet());
    }

    private void updateStatus(Epic epic) {
        Set<SubTask> subTaskSet = epic.getSubTaskSet();
        long countNew = subTaskSet.stream().filter(val -> val.getStatus().equals(Status.NEW)).count();
        long countDone = subTaskSet.stream().filter(val -> val.getStatus().equals(Status.DONE)).count();
        if (subTaskSet.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (countNew == subTaskSet.size()) {
            epic.setStatus(Status.NEW);
        } else if (countDone == subTaskSet.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
