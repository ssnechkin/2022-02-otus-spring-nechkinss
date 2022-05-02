package ru.otus.homework.shell.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.otus.homework.shell.event.author.AddAuthorEvent;
import ru.otus.homework.shell.event.author.DeleteAuthorByIdEvent;
import ru.otus.homework.shell.event.author.EditAuthorEvent;
import ru.otus.homework.shell.event.author.OutputAllAuthorsEvent;
import ru.otus.homework.shell.event.book.*;
import ru.otus.homework.shell.event.genre.*;

@Service
public class EventsPublisherImpl implements EventsPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventsPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    @Override
    public void outputBook(String bookId) {
        applicationEventPublisher.publishEvent(new OutputBookEvent(bookId));
    }

    @Override
    public void outputAllBooks() {
        applicationEventPublisher.publishEvent(new OutputAllBooksEvent());
    }

    @Override
    public void addBook(String bookName) {
        applicationEventPublisher.publishEvent(new AddBookEvent(bookName));
    }

    @Override
    public void deleteBookById(String bookId) {
        applicationEventPublisher.publishEvent(new DeleteBookByIdEvent(bookId));
    }

    @Override
    public void addAnAuthorToABook(String bookId, String authorId) {
        applicationEventPublisher.publishEvent(new AddAnAuthorToABookEvent(bookId, authorId));
    }

    @Override
    public void addAnGenreToABook(String bookId, String genreId) {
        applicationEventPublisher.publishEvent(new AddAnGenreToABookEvent(bookId, genreId));
    }

    @Override
    public void addAnBookCommentToABook(String bookId, String comment) {
        applicationEventPublisher.publishEvent(new AddBookCommentEvent(bookId, comment));
    }

    @Override
    public void removeTheAuthorFromTheBook(String bookId, String authorId) {
        applicationEventPublisher.publishEvent(new RemoveTheAuthorFromTheBookEvent(bookId, authorId));
    }

    @Override
    public void removeTheGenreFromTheBook(String bookId, String genreId) {
        applicationEventPublisher.publishEvent(new RemoveTheGenreFromTheBookEvent(bookId, genreId));
    }

    @Override
    public void removeTheBookCommentFromTheBook(String bookCommentId) {
        applicationEventPublisher.publishEvent(new RemoveBookCommentEvent(bookCommentId));
    }

    @Override
    public void outputAllAuthors() {
        applicationEventPublisher.publishEvent(new OutputAllAuthorsEvent());
    }

    @Override
    public void addAuthor(String surname, String name, String patronymic) {
        applicationEventPublisher.publishEvent(new AddAuthorEvent(surname, name, patronymic));
    }

    @Override
    public void deleteAuthorById(String authorId) {
        applicationEventPublisher.publishEvent(new DeleteAuthorByIdEvent(authorId));
    }

    @Override
    public void outputAllGenres() {
        applicationEventPublisher.publishEvent(new OutputAllGenresEvent());
    }

    @Override
    public void addGenre(String genreName) {
        applicationEventPublisher.publishEvent(new AddGenreEvent(genreName));
    }

    @Override
    public void deleteGenreById(String genreId) {
        applicationEventPublisher.publishEvent(new DeleteGenreByIdEvent(genreId));
    }

    @Override
    public void setGenreDescription(String genreId, String description) {
        applicationEventPublisher.publishEvent(new SetGenreDescriptionEvent(genreId, description));
    }

    @Override
    public void updateBookComment(String bookCommentId, String comment) {
        applicationEventPublisher.publishEvent(new UpdateBookCommentEvent(bookCommentId, comment));
    }

    @Override
    public void updateBookName(String bookId, String name) {
        applicationEventPublisher.publishEvent(new UpdateBookNameEvent(bookId, name));
    }

    @Override
    public void outputBookComments(String bookId) {
        applicationEventPublisher.publishEvent(new OutputBookCommentsEvent(bookId));
    }

    @Override
    public void editAuthor(String authorId, String surname, String name, String patronymic) {
        applicationEventPublisher.publishEvent(new EditAuthorEvent(authorId, surname, name, patronymic));
    }

    @Override
    public void editGenre(String genreId, String genreName) {
        applicationEventPublisher.publishEvent(new EditGenreEvent(genreId, genreName));
    }
}
