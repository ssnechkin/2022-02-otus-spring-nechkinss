package ru.otus.homework.dao.book;

import ru.otus.homework.dao.ext.GetListByIdDao;
import ru.otus.homework.dao.ext.InsertDao;
import ru.otus.homework.domain.BookGenre;

public interface BookGenreDao extends InsertDao<BookGenre>, GetListByIdDao<BookGenre> {
    boolean isExist(long bookId, long genreId);

    void delete(long bookId, long genreId);

    void deleteLinks(long genreId);
}
