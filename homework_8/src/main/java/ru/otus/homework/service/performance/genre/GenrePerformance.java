package ru.otus.homework.service.performance.genre;

import ru.otus.homework.service.performance.Performance;

public interface GenrePerformance extends Performance {

    String add(String name);

    String editDescription(String id, String description);

    String edit(String id, String name);
}
