package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class OutputBookEvent extends ApplicationEvent {
    @Getter
    private final long bookId;

    public OutputBookEvent(long bookId) {
        super(bookId);
        this.bookId = bookId;
    }
}
