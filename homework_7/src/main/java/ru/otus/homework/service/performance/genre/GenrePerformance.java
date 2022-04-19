package ru.otus.homework.service.performance.genre;

import ru.otus.homework.entity.Genre;
import ru.otus.homework.service.performance.Performance;

public interface GenrePerformance extends Performance<Genre> {
    void outputSetDescription(long id, String description);
}
