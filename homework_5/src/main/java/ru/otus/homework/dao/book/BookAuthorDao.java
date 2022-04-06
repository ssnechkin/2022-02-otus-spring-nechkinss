package ru.otus.homework.dao.book;

import ru.otus.homework.dao.ext.GetListByIdDao;
import ru.otus.homework.dao.ext.InsertDao;
import ru.otus.homework.domain.BookAuthor;

public interface BookAuthorDao extends InsertDao<BookAuthor>, GetListByIdDao<BookAuthor> {
    boolean isExist(long bookId, long authorId);

    void delete(long bookId, long authorId);

    void deleteLinks(long authorId);
}
