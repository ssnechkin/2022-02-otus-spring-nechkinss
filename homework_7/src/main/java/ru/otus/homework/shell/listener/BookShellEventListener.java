package ru.otus.homework.shell.listener;

import org.springframework.stereotype.Component;
import ru.otus.homework.service.book.BookService;
import ru.otus.homework.shell.event.book.*;

@Component
public class BookShellEventListener {

    private final BookService bookService;

    public BookShellEventListener(BookService bookService) {
        this.bookService = bookService;
    }

    @org.springframework.context.event.EventListener
    public void addBookEvent(AddBookEvent event) {
        bookService.add(event.getBookName());
    }

    @org.springframework.context.event.EventListener
    public void addAnAuthorToABookEvent(AddAnAuthorToABookEvent event) {
        bookService.addAuthor(event.getBookId(), event.getAuthorId());
    }

    @org.springframework.context.event.EventListener
    public void addAnGenreToABookEvent(AddAnGenreToABookEvent event) {
        bookService.addGenre(event.getBookId(), event.getGenreId());
    }

    @org.springframework.context.event.EventListener
    public void deleteBookByIdEvent(DeleteBookByIdEvent event) {
        bookService.delete(event.getBookId());
    }

    @org.springframework.context.event.EventListener
    public void outputAllBooksEvent(OutputAllBooksEvent event) {
        bookService.outputAll();
    }

    @org.springframework.context.event.EventListener
    public void outputBookEvent(OutputBookEvent event) {
        bookService.output(event.getBookId());
    }

    @org.springframework.context.event.EventListener
    public void removeTheAuthorFromTheBookEvent(RemoveTheAuthorFromTheBookEvent event) {
        bookService.removeAuthor(event.getBookId(), event.getAuthorId());
    }

    @org.springframework.context.event.EventListener
    public void removeTheGenreFromTheBookEvent(RemoveTheGenreFromTheBookEvent event) {
        bookService.removeGenre(event.getBookId(), event.getGenreId());
    }

    @org.springframework.context.event.EventListener
    public void addBookCommentEvent(AddBookCommentEvent event) {
        bookService.addComment(event.getBookId(), event.getComment());
    }

    @org.springframework.context.event.EventListener
    public void removeBookCommentEvent(RemoveBookCommentEvent event) {
        bookService.removeBookComment(event.getBookCommentId());
    }

    @org.springframework.context.event.EventListener
    public void updateBookCommentEvent(UpdateBookCommentEvent event) {
        bookService.updateBookComment(event.getBookCommentId(), event.getComment());
    }

    @org.springframework.context.event.EventListener
    public void updateBookNameEvent(UpdateBookNameEvent event) {
        bookService.updateBookName(event.getBookId(), event.getName());
    }

    @org.springframework.context.event.EventListener
    public void outputBookCommentsEvent(OutputBookCommentsEvent event) {
        bookService.outputBookComments(event.getBookId());
    }
}