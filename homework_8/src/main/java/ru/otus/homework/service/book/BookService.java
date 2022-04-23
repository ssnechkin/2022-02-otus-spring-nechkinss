package ru.otus.homework.service.book;

import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.book.Book;
import ru.otus.homework.domain.book.BookComment;
import ru.otus.homework.domain.Genre;

import java.util.List;

public interface BookService {

    Book add(String name);

    Book getById(String id);

    BookComment getBookCommentById(String id);

    List<Book> getAll();

    Book editName(Book book, String name);

    boolean addAuthor(Book book, Author author);

    boolean deleteAuthor(Book book, Author author);

    boolean addGenre(Book book, Genre genre);

    boolean deleteGenre(Book book, Genre genre);

    BookComment addComment(Book book, String comment);

    BookComment editComment(BookComment bookComment, String comment);

    void deleteComment(BookComment bookComment);

    void delete(Book book);
}
