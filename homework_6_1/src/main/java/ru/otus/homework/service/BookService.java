package ru.otus.homework.service;

public interface BookService {

    void add(String bookName);

    void addAuthor(long bookId, long authorId);

    void addGenre(long bookId, long genreId);

    void addComment(long bookId, String comment);

    void delete(long bookId);

    void outputAll();

    void outputBookComments(long bookId);

    void output(long bookId);

    void removeAuthor(long bookId, long authorId);

    void removeGenre(long bookId, long genreId);

    void removeBookComment(long bookCommentId);

    void updateBookComment(long bookCommentId, String comment);
}
