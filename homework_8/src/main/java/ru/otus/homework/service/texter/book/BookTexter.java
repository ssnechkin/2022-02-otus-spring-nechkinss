package ru.otus.homework.service.texter.book;

import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.domain.book.Book;
import ru.otus.homework.domain.book.BookComment;
import ru.otus.homework.service.texter.Texter;

import java.util.List;

public interface BookTexter extends Texter<Book> {

    String toString(Book book, List<Author> authorList, List<Genre> genreList);

    String edit(Book book, List<Author> authorList, List<Genre> genreList);

    String outputBookComment(BookComment bookComment);

    String authorAlreadyAdded(Book book, Author author);

    String authorAdded(Book book, Author author);

    String genreAlreadyAdded(Book book, Genre genre);

    String genreAdded(Book book, Genre genre);

    String commentAdded(Book book, BookComment bookComment);

    String totalComments(long count);

    String deleteAuthor(Book book, Author author);

    String authorMissing(Book book, Author author);

    String deleteGenre(Book book, Genre genre);

    String genreMissing(Book book, Genre genre);

    String commentNotFound(String id);

    String deleteComment(String id, String comment);

    String updateComment(BookComment bookComment, String comment);
}
