package ru.otus.homework.service.author;

public interface AuthorService {
    void add(String surname, String name, String patronymic);

    void delete(String authorId);

    void outputAll();
}
