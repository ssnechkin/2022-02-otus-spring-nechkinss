package ru.otus.homework.service.book;

import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.book.BookComment;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.dto.out.content.Button;

import java.util.List;

public interface BookService {

    Book add(String name);

    Book getById(long id);

    BookComment getBookCommentById(long id);

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
