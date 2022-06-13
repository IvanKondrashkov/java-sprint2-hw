package ru.yandex.praktikum.entity;

import lombok.Setter;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import java.util.*;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
public class Epic extends Task {
    private transient Set<SubTask> subTaskSet;

    public Epic(TypeTask type, String name, String description, Status status, int duration, LocalDateTime startTime) {
        super(type, name, description, status, duration, startTime);
        this.subTaskSet = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
    }

    public Epic(long id, TypeTask type, String name, String description, Status status, int duration, LocalDateTime startTime) {
        super(id, type, name, description, status, duration, startTime);
        this.subTaskSet = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s,", id, type, name, description, status, duration, startTime, endTime);
    }
}
