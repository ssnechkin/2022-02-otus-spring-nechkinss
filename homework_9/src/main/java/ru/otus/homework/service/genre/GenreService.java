package ru.otus.homework.service.genre;

import ru.otus.homework.domain.entity.genre.Genre;

import java.util.List;

public interface GenreService {

    Genre add(String name);

    Genre getById(long id);

    List<Genre> getAll();

    Genre editDescription(Genre genre, String description);

    Genre editName(Genre genre, String name);

    void delete(Genre genre);
}