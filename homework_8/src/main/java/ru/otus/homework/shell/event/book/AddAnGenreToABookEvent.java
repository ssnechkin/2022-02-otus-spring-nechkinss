package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class AddAnGenreToABookEvent extends ApplicationEvent {

    @Getter
    private final String bookId, genreId;

    public AddAnGenreToABookEvent(String bookId, String genreId) {
        super(Map.of("bookId", bookId, "genreId", genreId));
        this.bookId = bookId;
        this.genreId = genreId;
    }
}
