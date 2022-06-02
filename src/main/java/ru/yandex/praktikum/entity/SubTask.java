package ru.yandex.praktikum.entity;

import lombok.Setter;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
public class SubTask extends Task {
    private long epicId;

    public SubTask(TypeTask type, String name, String description, Status status, int duration, LocalDateTime startTime, long epicId) {
        super(type, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(long id, TypeTask type, String name, String description, Status status, int duration, LocalDateTime startTime, long epicId) {
        super(id, type, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s,%d", id, type, name, description, status, duration, startTime, endTime, epicId);
    }
}
