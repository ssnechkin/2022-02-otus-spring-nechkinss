package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class OutputBookEvent extends ApplicationEvent {

    @Getter
    private final String bookId;

    public OutputBookEvent(String bookId) {
        super(bookId);
        this.bookId = bookId;
    }
}
