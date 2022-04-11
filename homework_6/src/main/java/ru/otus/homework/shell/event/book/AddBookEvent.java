package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class AddBookEvent extends ApplicationEvent {
    @Getter
    private final String bookName;

    public AddBookEvent(String bookName) {
        super(bookName);
        this.bookName = bookName;
    }
}
