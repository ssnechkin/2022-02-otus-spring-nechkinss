package ru.otus.homework.repository.book.comment;

import ru.otus.homework.entity.BookComment;
import ru.otus.homework.repository.ext.DeleteDao;
import ru.otus.homework.repository.ext.GetByIdDao;
import ru.otus.homework.repository.ext.InsertDao;
import ru.otus.homework.repository.ext.UpdateDao;

import java.util.List;

public interface BookCommentDao extends InsertDao<BookComment>, UpdateDao<BookComment>, DeleteDao,
        GetByIdDao<BookComment> {
    List<BookComment> getAll(long bookId);
}
