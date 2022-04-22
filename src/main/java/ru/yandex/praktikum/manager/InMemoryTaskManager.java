package ru.yandex.praktikum.manager;

import java.util.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.praktikum.entity.Epic;
import ru.yandex.praktikum.entity.Task;
import ru.yandex.praktikum.entity.SubTask;
import ru.yandex.praktikum.entity.Status;
import ru.yandex.praktikum.utils.Managers;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicLong;

@Setter
@Getter
public class InMemoryTaskManager implements TaskManager {
    private static final AtomicLong idCurrent = new AtomicLong();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private Map<Long, Epic> epics;
    private Map<Long, Task> tasks;
    private Map<Long, SubTask> subtasks;

    public InMemoryTaskManager() {
        this.epics = new HashMap<>();
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    private static long getIdCurrent() {
        return idCurrent.incrementAndGet();
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(InMemoryTaskManager.getIdCurrent());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Task addTask(Task task) {
        task.setId(InMemoryTaskManager.getIdCurrent());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask addSubTask(SubTask subTask) {
        subTask.setId(InMemoryTaskManager.getIdCurrent());
        Epic epic = epics.get(subTask.getEpicId());

        if (epics.containsKey(epic.getId())) {
            Set<SubTask> subTaskSet = epic.getSubTaskSet();
            subTaskSet.add(subTask);
            subtasks.put(subTask.getId(), subTask);
        }
        updateEpic(epic);
        return subTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            updateStatus(epic);
            epics.put(epic.getId(), epic);
        }
        return epic;
    }

    @Override
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
        return task;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());

        if (subtasks.containsKey(subTask.getId())) {
            subtasks.put(subTask.getId(), subTask);
        }
        updateEpic(epic);
        return subTask;
    }

    @Override
    public Epic getEpicById(long id) {
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public Task getTaskById(long id) {
        Task task = tasks.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public SubTask getSubTaskById(long id) {
        SubTask subTask = subtasks.get(id);
        historyManager.addTask(subTask);
        return subTask;
    }

    @Override
    public void deleteEpicById(long id) {
        Epic epic = epics.get(id);
        Set<SubTask> subTaskSet = epic.getSubTaskSet();

        if (epics.containsKey(id)) {
            epics.remove(id);
            subTaskSet.forEach(it -> historyManager.remove(it.getId()));
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteTaskById(long id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(long id) {
        SubTask subTask = subtasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());

        if (subtasks.containsKey(id)) {
            Set<SubTask> subTaskSet = epic.getSubTaskSet();
            subTaskSet = subTaskSet.stream().filter(it -> !it.equals(subTask)).collect(Collectors.toSet());
            epic.setSubTaskSet(subTaskSet);
            subtasks.remove(id);
            historyManager.remove(id);
        }
        updateEpic(epic);
    }

    @Override
    public void deleteAllEpic() {
        if (!epics.isEmpty()) {
            epics.clear();
        }
    }

    @Override
    public void deleteAllTask() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    @Override
    public void deleteAllSubTask(Epic epic) {
        Set<SubTask> subTaskSet = epic.getSubTaskSet();

        if (!subtasks.isEmpty()) {
            subTaskSet.clear();
            subtasks.clear();
        }
        updateEpic(epic);
    }

    @Override
    public List<Epic> getListAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getListAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getListAllSubTask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<SubTask> getListAllSubTaskFromEpic(Epic epic) {
        return new ArrayList<>(epic.getSubTaskSet());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateStatus(Epic epic) {
        Set<SubTask> subTaskSet = epic.getSubTaskSet();
        boolean isAllNew = subTaskSet.stream().allMatch(it -> it.getStatus() == Status.NEW);
        boolean isAllDone = subTaskSet.stream().allMatch(it -> it.getStatus() == Status.DONE);

        if (isAllNew) {
            epic.setStatus(Status.NEW);
        } else if (isAllDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
