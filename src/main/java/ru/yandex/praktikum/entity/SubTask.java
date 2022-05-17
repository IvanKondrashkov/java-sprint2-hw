package ru.yandex.praktikum.entity;

import lombok.Setter;
import lombok.Getter;
import java.util.Objects;

@Setter
@Getter
public class SubTask extends Task {
    private long epicId;

    public SubTask(TypesTasks type, String name, String description, Status status, long epicId) {
        super(type, name, description, status);
        this.epicId = epicId;
    }

    public SubTask(long id, TypesTasks type, String name, String description, Status status, long epicId) {
        super(id, type, name, description, status);
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d", id, type, name, description, status, epicId);
    }
}
