package ru.otus.homework.service.author;

public interface AuthorService {
    void add(String surname, String name, String patronymic);

    void delete(long authorId);

    void outputAll();

    void edit(long authorId, String surname, String name, String patronymic);
}
