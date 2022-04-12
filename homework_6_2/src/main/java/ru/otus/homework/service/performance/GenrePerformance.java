package ru.otus.homework.service.performance;

import ru.otus.homework.entity.Genre;

public interface GenrePerformance extends Performance<Genre> {
    void outputSetDescription(long id, String description);
}
