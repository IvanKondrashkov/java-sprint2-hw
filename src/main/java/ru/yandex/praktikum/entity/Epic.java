package ru.yandex.praktikum.entity;

import lombok.Setter;
import lombok.Getter;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

@Setter
@Getter
public class Epic extends Task {
    private Set<SubTask> subTaskSet;

    public Epic(TypesTasks type, String name, String description, Status status) {
        super(type, name, description, status);
        this.subTaskSet = new HashSet<>();
    }

    public Epic(long id, TypesTasks type, String name, String description, Status status) {
        super(id, type, name, description, status);
        this.subTaskSet = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskSet, epic.subTaskSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskSet);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,", id, type, name, description, status);
    }
}
