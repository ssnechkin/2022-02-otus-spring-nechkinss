package ru.otus.homework.service.texter.book;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.domain.book.Book;
import ru.otus.homework.domain.book.BookComment;

import java.util.List;

@Service
public class BookTexterImpl implements BookTexter {

    @Override
    public String add(Book book) {
        return "Book added. ID: " + book.getId();
    }

    @Override
    public String total(long count) {
        return "Total books: " + count;
    }

    @Override
    public String toString(Book book, List<Author> authorList, List<Genre> genreList) {
        return "Book"
                + " ID: " + book.getId()
                + " Name: " + book.getName()
                + " Authors: " + authorsToString(authorList)
                + " Genres: " + genresToString(genreList);
    }

    @Override
    public String edit(Book book, List<Author> authorList, List<Genre> genreList) {
        return "Edit Book"
                + " ID: " + book.getId()
                + " Name: " + book.getName()
                + " Authors: " + authorsToString(authorList)
                + " Genres: " + genresToString(genreList);
    }

    @Override
    public String outputBookComment(BookComment bookComment) {
        return "BookComment ID: " + bookComment.getId() + " Comment: " + bookComment.getComment();
    }

    @Override
    public String authorAlreadyAdded(Book book, Author author) {
        return "The author has already been added to the selected book ID: " + book.getId()
                + " Author ID: " + author.getId();
    }

    @Override
    public String authorAdded(Book book, Author author) {
        return "The author added to the selected book ID: " + book.getId() + " Author ID: " + author.getId();
    }

    @Override
    public String genreAlreadyAdded(Book book, Genre genre) {
        return "The genre has already been added to the selected book ID: " + book.getId()
                + " Genre ID: " + genre.getId();
    }

    @Override
    public String genreAdded(Book book, Genre genre) {
        return "The genre added to the selected book ID: " + genre.getId() + " Genre ID: " + genre.getId();
    }

    @Override
    public String commentAdded(Book book, BookComment bookComment) {
        return "The comment added to the selected book ID: " + book.getId()
                + " commentId: " + bookComment.getId() + " comment: " + bookComment.getComment();
    }

    @Override
    public String totalComments(long count) {
        return "Total comments: " + count;
    }

    @Override
    public String deleteAuthor(Book book, Author author) {
        return "The author has been removed from the book. BookId: " + book.getId()
                + " AuthorId: " + author.getId();
    }

    @Override
    public String authorMissing(Book book, Author author) {
        return "The author with the ID=" + author.getId() + " is missing from the book ID: " + book.getId();
    }

    @Override
    public String deleteGenre(Book book, Genre genre) {
        return "The genre has been removed from the book. BookId: " + book.getId()
                + " GenreId: " + genre.getId();
    }

    @Override
    public String genreMissing(Book book, Genre genre) {
        return "The genre with the ID=" + genre.getId() + " is missing from the book ID: " + book.getId();
    }

    @Override
    public String commentNotFound(String id) {
        return "The comment was not found by ID: " + id;
    }

    @Override
    public String deleteComment(String id, String comment) {
        return "The comment has been removed from the book."
                + " commentId: " + id
                + " comment: " + comment;
    }

    @Override
    public String updateComment(BookComment bookComment, String comment) {
        return "The comment has been update in the book."
                + " commentId: " + bookComment.getId()
                + " comment: " + comment;
    }

    @Override
    public String notFound(String id) {
        return "The book was not found by ID: " + id;
    }

    @Override
    public String delete(String id) {
        return "Book deleted. ID: " + id;
    }

    private String authorsToString(List<Author> authorList) {
        StringBuilder authors = new StringBuilder();
        for (Author author : authorList) {
            authors.append("id: ").append(author.getId()).append(" ")
                    .append(author.getSurname()).append(" ")
                    .append(author.getName()).append(" ")
                    .append(author.getPatronymic()).append("; ");
        }
        return authors.toString();
    }

    private String genresToString(List<Genre> genreList) {
        StringBuilder genres = new StringBuilder();
        for (Genre genre : genreList) {
            genres.append("id: ").append(genre.getId()).append(" ")
                    .append(genre.getName()).append("; ");
        }
        return genres.toString();
    }
}
