package ru.otus.homework.service.genre;

import ru.otus.homework.entity.Genre;
import ru.otus.homework.service.ext.Performance;

public interface GenrePerformance extends Performance<Genre> {
    void outputSetDescription(String id, String description);
}
