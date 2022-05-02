package ru.otus.homework.service.author;

import ru.otus.homework.domain.entity.author.Author;

import java.util.List;

public interface AuthorService {

    Author add(String surname, String name, String patronymic);

    Author getById(long id);

    List<Author> getAll();

    Author edit(Author author, String surname, String name, String patronymic);

    void delete(Author author);
}
