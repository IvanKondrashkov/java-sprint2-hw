package ru.yandex.praktikum.manager;

import lombok.Getter;
import lombok.Setter;
import java.util.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import ru.yandex.praktikum.entity.Epic;
import ru.yandex.praktikum.entity.Task;
import ru.yandex.praktikum.entity.SubTask;
import ru.yandex.praktikum.entity.Status;

import java.util.concurrent.atomic.AtomicLong;

@Setter
@Getter
public class InMemoryTaskManager implements TaskManager {
    protected static AtomicLong idCurrent = new AtomicLong();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected Map<Long, Epic> epics;
    protected Map<Long, Task> tasks;
    protected Map<Long, SubTask> subtasks;
    protected Map<Duration, Boolean> grid;

    public InMemoryTaskManager() {
        this.epics = new HashMap<>();
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.grid = fillGrid();
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
        validateDateTimeTask(task);
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
            validateDateTimeTask(subTask);
            subtasks.put(subTask.getId(), subTask);
        }
        updateEpic(epic);
        return subTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            updateStatus(epic);
            updateDateTime(epic);
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
            subTaskSet.forEach(it -> {
                subtasks.remove(it.getId());
                historyManager.remove(it.getId());
            });
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
            subTaskSet = subTaskSet.stream()
                    .filter(it -> !it.equals(subTask))
                    .collect(Collectors.toSet());

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

    @Override
    public List<Task> getPrioritizedTasks() {
        Set<Task> set = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
        set.addAll(tasks.values());
        set.addAll(subtasks.values());
        return new ArrayList<>(set);
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

    private void updateDateTime(Epic epic) {
        Set<SubTask> subTaskSet = epic.getSubTaskSet();
        Optional<SubTask> subTask = subTaskSet.stream().findFirst();
        int duration = subTaskSet.stream()
                .mapToInt(SubTask::getDuration)
                .sum();

        if (subTask.isPresent()) {
            LocalDateTime startTime = subTask.get().getStartTime();
            LocalDateTime endTime = startTime.plusMinutes(duration);

            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(duration);
        }
    }

    private void validateDateTimeTask(Task task) {
        Optional<LocalDateTime> startTime = Optional.ofNullable(task.getStartTime());
        Optional<LocalDateTime> endTime = Optional.ofNullable(task.getEndTime());
        Duration duration;

        if (startTime.isPresent()) {
            duration = Duration.between(startTime.get(), endTime.get());
            List<Task> priority = getPrioritizedTasks();

            for (Map.Entry<Duration, Boolean> entry : grid.entrySet()) {
                long remains = duration.toMinutes() % 15;
                long whole = duration.toMinutes() - remains;
                if (entry.getValue()) {
                    if (duration.toMinutes() % 15 == 0) {
                        grid.put(entry.getKey(), false);
                        duration = duration.minusMinutes(15);
                    } else if (whole != 0) {
                        grid.put(entry.getKey(), false);
                        duration = duration.minusMinutes(15);
                        whole -= 15;
                    } else {
                        grid.put(entry.getKey(), false);
                        duration = duration.minusMinutes(remains);
                    }
                } else {
                    int count = (int) priority.stream()
                            .filter(it -> it.getStartTime() != null)
                            .count();

                    if (priority.get(count - 1).getEndTime().isAfter(task.getStartTime())) {
                        throw new IllegalArgumentException("Start time conflict task id=" + task.getId());
                    }
                }
                if (duration.isZero()) {
                    break;
                }
            }
        }
    }

    private Map<Duration, Boolean> fillGrid() {
        grid = new LinkedHashMap<>();
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(365);
        Duration duration = Duration.between(startTime, endTime);
        Duration interval = Duration.ofMinutes(15);

        for (long i = 0; i < duration.toMinutes(); i = i + 15) {
            grid.put(interval, true);
            interval = interval.plusMinutes(15);
        }
        return grid;
    }
}
