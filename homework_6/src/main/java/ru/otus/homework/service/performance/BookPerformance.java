package ru.otus.homework.service.performance;

import ru.otus.homework.entity.Book;

public interface BookPerformance extends Performance<Book> {
    void outputBookComment(long bookId, String comment);

    void authorAlreadyAdded(long bookId, long authorId);

    void authorAdded(long bookId, long authorId);

    void genreAlreadyAdded(long bookId, long genreId);

    void genreAdded(long bookId, long genreId);

    void commentAdded(long bookId, long commentId, String comment);

    void totalComments(long count);

    void authorRemoved(long bookId, long authorId);

    void authorMissing(long bookId, long authorId);

    void genreRemoved(long bookId, long genreId);

    void genreMissing(long bookId, long genreId);

    void commentNotFound(long commentId);

    void removeComment(long commentId, String comment);

    void updateComment(long bookCommentId, String comment);

    void updateName(long bookId, String oldName, String newName);
}
