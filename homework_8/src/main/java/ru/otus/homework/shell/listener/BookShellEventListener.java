package ru.otus.homework.shell.listener;

import org.springframework.stereotype.Component;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.performance.book.BookPerformance;
import ru.otus.homework.shell.event.book.*;

@Component
public class BookShellEventListener {

    private final BookPerformance bookPerformance;
    private final IOService ioService;

    public BookShellEventListener(BookPerformance bookPerformance, IOService ioService) {
        this.bookPerformance = bookPerformance;
        this.ioService = ioService;
    }

    @org.springframework.context.event.EventListener
    public void addBookEvent(AddBookEvent event) {
        ioService.outputString(bookPerformance.add(event.getBookName()));
    }

    @org.springframework.context.event.EventListener
    public void outputBookEvent(OutputBookEvent event) {
        ioService.outputString(bookPerformance.getById(event.getBookId()));
    }

    @org.springframework.context.event.EventListener
    public void outputAllBooksEvent(OutputAllBooksEvent event) {
        for (String line : bookPerformance.getAll()) {
            ioService.outputString(line);
        }
    }

    @org.springframework.context.event.EventListener
    public void addAnAuthorToABookEvent(AddAnAuthorToABookEvent event) {
        ioService.outputString(bookPerformance.addAuthor(event.getBookId(), event.getAuthorId()));
    }

    @org.springframework.context.event.EventListener
    public void addAnGenreToABookEvent(AddAnGenreToABookEvent event) {
        ioService.outputString(bookPerformance.addGenre(event.getBookId(), event.getGenreId()));
    }

    @org.springframework.context.event.EventListener
    public void deleteBookByIdEvent(DeleteBookByIdEvent event) {
        ioService.outputString(bookPerformance.deleteById(event.getBookId()));
    }

    @org.springframework.context.event.EventListener
    public void removeTheAuthorFromTheBookEvent(RemoveTheAuthorFromTheBookEvent event) {
        ioService.outputString(bookPerformance.deleteAuthor(event.getBookId(), event.getAuthorId()));
    }

    @org.springframework.context.event.EventListener
    public void removeTheGenreFromTheBookEvent(RemoveTheGenreFromTheBookEvent event) {
        ioService.outputString(bookPerformance.deleteGenre(event.getBookId(), event.getGenreId()));
    }

    @org.springframework.context.event.EventListener
    public void addBookCommentEvent(AddBookCommentEvent event) {
        ioService.outputString(bookPerformance.addComment(event.getBookId(), event.getComment()));
    }

    @org.springframework.context.event.EventListener
    public void removeBookCommentEvent(RemoveBookCommentEvent event) {
        ioService.outputString(bookPerformance.deleteCommentByCommentId(event.getBookCommentId()));
    }

    @org.springframework.context.event.EventListener
    public void updateBookCommentEvent(UpdateBookCommentEvent event) {
        ioService.outputString(bookPerformance.editCommentByCommentId(event.getBookCommentId(), event.getComment()));
    }

    @org.springframework.context.event.EventListener
    public void updateBookNameEvent(UpdateBookNameEvent event) {
        ioService.outputString(bookPerformance.edit(event.getBookId(), event.getName()));
    }

    @org.springframework.context.event.EventListener
    public void outputBookCommentsEvent(OutputBookCommentsEvent event) {
        for (String line : bookPerformance.getComments(event.getBookId())) {
            ioService.outputString(line);
        }
    }
}