package ru.otus.homework.dao.author;

import ru.otus.homework.dao.ext.*;
import ru.otus.homework.domain.Author;

public interface AuthorDao extends SequenceDao, CountDao,
        InsertDao<Author>, UpdateDao<Author>, DeleteDao,
        GetByIdDao<Author>, GetAllDao<Author> {
}
