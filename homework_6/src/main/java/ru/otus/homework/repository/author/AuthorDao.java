package ru.otus.homework.repository.author;

import ru.otus.homework.entity.Author;
import ru.otus.homework.repository.ext.*;

public interface AuthorDao extends InsertDao<Author>, UpdateDao<Author>, DeleteDao,
        GetByIdDao<Author>, GetAllDao<Author> {
}
