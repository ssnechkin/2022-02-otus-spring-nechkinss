package ru.otus.homework.service.book;

import org.springframework.stereotype.Service;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.service.io.IOServiceStreams;

@Service
public class BookPerformanceImpl implements BookPerformance {
    private final IOServiceStreams ioService;

    public BookPerformanceImpl(IOServiceStreams ioService) {
        this.ioService = ioService;
    }

    @Override
    public void delete(String id) {
        ioService.outputString("Book deleted. ID: " + id);
    }

    @Override
    public void add(String id) {
        ioService.outputString("Book added. ID: " + id);
    }

    @Override
    public void total(long count) {
        ioService.outputString("Total books: " + count);
    }

    @Override
    public void notFound(String id) {
        ioService.outputString("The book was not found by ID: " + id);
    }

    @Override
    public void output(Book book) {
        ioService.outputString("Book"
                + " ID: " + book.getId()
                + " Name: " + book.getName()
                + " Authors: " + getAuthors(book)
                + " Genres: " + getGenres(book)
        );
    }

    @Override
    public void outputBookComment(String bookId, String comment) {
        ioService.outputString("BookComment ID: " + bookId + " Comment: " + comment);
    }

    @Override
    public void authorAlreadyAdded(String bookId, String authorId) {
        ioService.outputString("The author has already been added to the selected book ID: " + bookId
                + " Author ID: " + authorId);
    }

    @Override
    public void authorAdded(String bookId, String authorId) {
        ioService.outputString("The author added to the selected book ID: " + bookId + " Author ID: " + authorId);
    }

    @Override
    public void genreAlreadyAdded(String bookId, String genreId) {
        ioService.outputString("The genre has already been added to the selected book ID: " + bookId
                + " Genre ID: " + genreId);
    }

    @Override
    public void genreAdded(String bookId, String genreId) {
        ioService.outputString("The genre added to the selected book ID: " + bookId + " Genre ID: " + genreId);
    }

    @Override
    public void commentAdded(String bookId, String commentId, String comment) {
        ioService.outputString("The comment added to the selected book ID: " + bookId
                + " commentId: " + commentId + " comment: " + comment);
    }

    @Override
    public void totalComments(long count) {
        ioService.outputString("Total comments: " + count);
    }

    @Override
    public void authorRemoved(String bookId, String authorId) {
        ioService.outputString("The author has been removed from the book. BookId: " + bookId
                + " AuthorId: " + authorId);
    }

    @Override
    public void authorMissing(String bookId, String authorId) {
        ioService.outputString("The author with the ID=" + authorId + " is missing from the book ID: " + bookId);
    }

    @Override
    public void genreRemoved(String bookId, String genreId) {
        ioService.outputString("The genre has been removed from the book. BookId: " + bookId
                + " GenreId: " + genreId);
    }

    @Override
    public void genreMissing(String bookId, String genreId) {
        ioService.outputString("The genre with the ID=" + genreId + " is missing from the book ID: " + bookId);
    }

    @Override
    public void commentNotFound(String commentId) {
        ioService.outputString("The comment was not found by ID: " + commentId);
    }

    @Override
    public void removeComment(String commentId, String comment) {
        ioService.outputString("The comment has been removed from the book."
                + " commentId: " + comment
                + " comment: " + comment);
    }

    @Override
    public void updateComment(String bookCommentId, String comment) {
        ioService.outputString("The comment has been update in the book."
                + " commentId: " + bookCommentId
                + " comment: " + comment);
    }

    @Override
    public void updateName(String bookId, String oldName, String newName) {
        ioService.outputString("The book name has been update in the book."
                + " bookId: " + bookId
                + " oldName: " + oldName
                + " newName: " + newName
        );
    }

    private String getAuthors(Book book) {
        StringBuilder authors = new StringBuilder();
        if (book.getAuthors().size() > 0) {
            for (Author author : book.getAuthors()) {
                authors.append("id: ").append(author.getId()).append(" ")
                        .append(author.getSurname()).append(" ")
                        .append(author.getName()).append(" ")
                        .append(author.getPatronymic()).append("; ");
            }
        }
        return authors.toString();
    }

    private String getGenres(Book book) {
        StringBuilder genres = new StringBuilder();
        if (book.getGenres().size() > 0) {
            for (Genre genre : book.getGenres()) {
                genres.append("id: ").append(genre.getId()).append(" ")
                        .append(genre.getName()).append("; ");
            }
        }
        return genres.toString();
    }
}
