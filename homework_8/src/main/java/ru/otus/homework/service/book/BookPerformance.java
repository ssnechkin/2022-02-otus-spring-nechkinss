package ru.otus.homework.service.book;

import ru.otus.homework.entity.Book;
import ru.otus.homework.service.ext.Performance;

public interface BookPerformance extends Performance<Book> {
    void outputBookComment(String bookId, String comment);

    void authorAlreadyAdded(String bookId, String authorId);

    void authorAdded(String bookId, String authorId);

    void genreAlreadyAdded(String bookId, String genreId);

    void genreAdded(String bookId, String genreId);

    void commentAdded(String bookId, String commentId, String comment);

    void totalComments(long count);

    void authorRemoved(String bookId, String authorId);

    void authorMissing(String bookId, String authorId);

    void genreRemoved(String bookId, String genreId);

    void genreMissing(String bookId, String genreId);

    void commentNotFound(String commentId);

    void removeComment(String commentId, String comment);

    void updateComment(String bookCommentId, String comment);

    void updateName(String bookId, String oldName, String newName);
}
