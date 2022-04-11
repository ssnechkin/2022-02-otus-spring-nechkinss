package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeleteBookByIdEvent extends ApplicationEvent {
    @Getter
    private final long bookId;

    public DeleteBookByIdEvent(long bookId) {
        super(bookId);
        this.bookId = bookId;
    }
}
