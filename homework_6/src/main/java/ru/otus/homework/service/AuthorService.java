package ru.otus.homework.service;

public interface AuthorService {
    void add(String surname, String name, String patronymic);

    void delete(long authorId);

    void outputAll();
}
