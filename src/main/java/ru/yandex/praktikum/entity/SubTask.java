package ru.yandex.praktikum.entity;

import lombok.Setter;
import lombok.Getter;
import java.util.Objects;

@Setter
@Getter
public class SubTask extends Task {
    private long epicId;

    public SubTask(String name, String description, Status status, long epicId) {
        super(name, description, status);
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
        return "SubTask{" +
                "id=" + super.id +
                ", name='" + super.name + '\'' +
                ", description='" + super.description + '\'' +
                ", status=" + super.status +
                ", epicId=" + epicId +
                '}';
    }
}
