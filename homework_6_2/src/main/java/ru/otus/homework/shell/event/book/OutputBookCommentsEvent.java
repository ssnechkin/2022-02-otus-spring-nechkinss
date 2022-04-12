package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class OutputBookCommentsEvent extends ApplicationEvent {
    @Getter
    private final long bookId;

    public OutputBookCommentsEvent(long bookId) {
        super(Map.of("bookId", bookId));
        this.bookId = bookId;
    }
}
