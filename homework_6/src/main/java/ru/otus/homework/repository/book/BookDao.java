package ru.otus.homework.repository.book;

import ru.otus.homework.repository.ext.*;
import ru.otus.homework.entity.Book;

public interface BookDao extends CountDao,
        InsertDao<Book>, UpdateDao<Book>, DeleteDao,
        GetByIdDao<Book>, GetAllDao<Book> {
}
