package ru.yandex.praktikum.manager;

import lombok.Getter;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import ru.yandex.praktikum.entity.*;
import ru.yandex.praktikum.utils.CsvFormatterByHistory;
import ru.yandex.praktikum.exception.ManagerSaveException;

@Getter
public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public SubTask addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public Epic getEpicById(long id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Task getTaskById(long id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTaskById(long id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void deleteEpicById(long id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteTaskById(long id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(long id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllSubTask(Epic epic) {
        super.deleteAllSubTask(epic);
        save();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> list = super.getHistory();
        save();
        return list;
    }

    private Task fromString(String value) {
        final String[] fields = value.split(",");
        final TypeTask type = TypeTask.valueOf(fields[1]);
        switch (type) {
            case EPIC: {
                return new Epic(
                        Long.parseLong(fields[0]),
                        type,
                        fields[2],
                        fields[3],
                        Status.valueOf(fields[4]),
                        Integer.parseInt(fields[5]),
                        LocalDateTime.parse(fields[6])
                );
            }
            case TASK: {
                return new Task(
                        Long.parseLong(fields[0]),
                        type,
                        fields[2],
                        fields[3],
                        Status.valueOf(fields[4]),
                        Integer.parseInt(fields[5]),
                        fields[6].equals("null") ? null : LocalDateTime.parse(fields[6])
                );
            }
            case SUBTASK: {
                return new SubTask(
                        Long.parseLong(fields[0]),
                        type,
                        fields[2],
                        fields[3],
                        Status.valueOf(fields[4]),
                        Integer.parseInt(fields[5]),
                        fields[6].equals("null") ? null : LocalDateTime.parse(fields[6]),
                        Long.parseLong(fields[8])
                );
            }
            default: {
                throw new NoSuchElementException("There is no such task!");
            }
        }
    }

    protected void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String header = "id,type,name,status,description,duration,start,end,epic";
            writer.append(header);
            writer.newLine();
            for (Map.Entry<Long, Task> entry : tasks.entrySet()) {
                writer.append(entry.getValue().toString());
                writer.newLine();
            }

            for (Map.Entry<Long, Epic> entry : epics.entrySet()) {
                writer.append(entry.getValue().toString());
                writer.newLine();
            }

            for (Map.Entry<Long, SubTask> entry : subtasks.entrySet()) {
                writer.append(entry.getValue().toString());
                writer.newLine();
            }

            String history = CsvFormatterByHistory.toString(historyManager);
            writer.newLine();
            writer.append(history);
        } catch (IOException e) {
            throw new ManagerSaveException("Saving to a file ended incorrectly!", e);
        }
    }

    protected void load() {
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            while (true) {
                String line = reader.readLine();
                if (!line.isEmpty()) {
                    Task task = fromString(line);
                    if (task.getType() == TypeTask.EPIC) {
                        epics.put(task.getId(), (Epic) task);
                    }
                    if (task.getType() == TypeTask.TASK) {
                        tasks.put(task.getId(), task);
                    }
                    if (task.getType() == TypeTask.SUBTASK) {
                        subtasks.put(task.getId(), (SubTask) task);
                    }
                } else {
                    break;
                }
            }

            if (reader.ready()) {
                String line = reader.readLine();
                List<Long> history = CsvFormatterByHistory.fromString(line);
                for (Long id : history) {
                    if (epics.containsKey(id)) {
                        Epic epic = epics.get(id);
                        historyManager.addTask(epic);
                    }
                    if (tasks.containsKey(id)) {
                        Task task = tasks.get(id);
                        historyManager.addTask(task);
                    }
                    if (subtasks.containsKey(id)) {
                        SubTask subTask = subtasks.get(id);
                        historyManager.addTask(subTask);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("The download from the file ended incorrectly!", e);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager manager = new FileBackedTasksManager(file);
        manager.load();
        return manager;
    }
}
