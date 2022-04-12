package ru.otus.homework.repository.book;

import ru.otus.homework.entity.Book;
import ru.otus.homework.repository.ext.*;

public interface BookDao extends InsertDao<Book>, UpdateDao<Book>, DeleteDao,
        GetByIdDao<Book>, GetAllDao<Book> {
}
