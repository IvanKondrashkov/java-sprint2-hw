package ru.yandex.praktikum.entity;

import lombok.Setter;
import lombok.Getter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
public class Epic extends Task {
    private Set<SubTask> subTaskSet;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
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
        return "Epic{" +
                "id=" + super.id +
                ", name='" + super.name + '\'' +
                ", description='" + super.description + '\'' +
                ", status=" + super.status +
                ", subTaskSet=" + subTaskSet +
                '}';
    }
}
