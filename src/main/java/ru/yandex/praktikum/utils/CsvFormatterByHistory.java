package ru.yandex.praktikum.utils;

import java.util.*;
import java.util.stream.Collectors;
import ru.yandex.praktikum.manager.HistoryManager;

public class CsvFormatterByHistory {
    public static String toString(HistoryManager manager) {
        return manager.getHistory().stream()
                .map(it -> String.valueOf(it.getId()))
                .collect(Collectors.joining(","));
    }

    public static List<Long> fromString(String value) {
        return Arrays.stream(value.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
