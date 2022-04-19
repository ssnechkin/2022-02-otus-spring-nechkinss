package ru.otus.homework.service.genre;

public interface GenreService {
    void delete(long genreId);

    void add(String genreName);

    void outputAll();

    void setDescription(long genreId, String description);
}
