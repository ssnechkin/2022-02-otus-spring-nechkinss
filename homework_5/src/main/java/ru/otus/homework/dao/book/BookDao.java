package ru.otus.homework.dao.book;

import ru.otus.homework.dao.ext.*;
import ru.otus.homework.domain.Book;

public interface BookDao extends SequenceDao, CountDao,
        InsertDao<Book>, UpdateDao<Book>, DeleteDao,
        GetByIdDao<Book>, GetAllDao<Book> {
}
