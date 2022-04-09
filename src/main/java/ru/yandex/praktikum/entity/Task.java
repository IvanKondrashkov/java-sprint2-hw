package ru.yandex.praktikum.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Setter
@Getter
public class Task {
    protected long id;
    protected String name;
    protected String description;
    protected Status status;

    public Task(String name, String description, Status status) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
