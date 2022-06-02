package ru.yandex.praktikum.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.Optional;

@Setter
@Getter
@EqualsAndHashCode
public class Task {
    protected long id;
    protected TypeTask type;
    protected String name;
    protected String description;
    protected Status status;
    protected int duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(TypeTask type, String name, String description, Status status, int duration, LocalDateTime startTime) {
        this.id = 0;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = Optional.ofNullable(startTime).orElse(startTime);
        this.endTime = Optional.ofNullable(startTime).isPresent() ? startTime.plusMinutes(duration) : null;
    }

    public Task(long id, TypeTask type, String name, String description, Status status, int duration, LocalDateTime startTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = Optional.ofNullable(startTime).orElse(startTime);
        this.endTime = Optional.ofNullable(startTime).isPresent() ? startTime.plusMinutes(duration) : null;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s,", id, type, name, description, status, duration, startTime, endTime);
    }
}
