package ru.otus.homework.service.genre;

public interface GenreService {
    void delete(String genreId);

    void add(String genreName);

    void outputAll();

    void setDescription(String genreId, String description);
}
