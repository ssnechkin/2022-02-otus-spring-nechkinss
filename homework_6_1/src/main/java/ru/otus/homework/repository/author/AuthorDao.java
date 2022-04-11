package ru.otus.homework.repository.author;

import ru.otus.homework.repository.ext.*;
import ru.otus.homework.entity.Author;

public interface AuthorDao extends CountDao,
        InsertDao<Author>, UpdateDao<Author>, DeleteDao,
        GetByIdDao<Author>, GetAllDao<Author> {
}
