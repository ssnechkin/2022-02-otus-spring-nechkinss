package ru.otus.homework.service.genre;

import ru.otus.homework.domain.Genre;

import java.util.List;

public interface GenreService {

    Genre add(String name);

    Genre getById(String id);

    List<Genre> getAll();

    Genre editDescription(Genre genre, String description);

    Genre editName(Genre genre, String name);

    void delete(Genre genre);
}