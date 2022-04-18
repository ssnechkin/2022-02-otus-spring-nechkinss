package ru.otus.homework.service.book;

import org.springframework.transaction.annotation.Transactional;

public interface BookService {

    void add(String bookName);

    void addAuthor(String bookId, String authorId);

    void addGenre(String bookId, String genreId);

    void addComment(String bookId, String comment);

    void delete(String bookId);

    void outputAll();

    void outputBookComments(String bookId);

    void output(String bookId);

    void removeAuthor(String bookId, String authorId);

    void removeGenre(String bookId, String genreId);

    void removeBookComment(String bookCommentId);

    void updateBookComment(String bookCommentId, String comment);

    @Transactional
    void updateBookName(String bookId, String newName);
}
