package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class RemoveTheGenreFromTheBookEvent extends ApplicationEvent {

    @Getter
    private final String bookId, genreId;

    public RemoveTheGenreFromTheBookEvent(String bookId, String genreId) {
        super(Map.of("bookId", bookId, "genreId", genreId));
        this.bookId = bookId;
        this.genreId = genreId;
    }
}
