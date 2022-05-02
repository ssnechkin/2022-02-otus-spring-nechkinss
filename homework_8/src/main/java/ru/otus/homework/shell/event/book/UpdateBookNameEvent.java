package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class UpdateBookNameEvent extends ApplicationEvent {

    @Getter
    private final String bookId;

    @Getter
    private final String name;

    public UpdateBookNameEvent(String bookId, String name) {
        super(Map.of("bookId", bookId, "name", name));
        this.bookId = bookId;
        this.name = name;
    }
}
