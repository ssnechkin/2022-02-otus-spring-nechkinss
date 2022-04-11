package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class SetBookCommentEvent extends ApplicationEvent {
    @Getter
    private final long bookId;
    @Getter
    private final String comment;

    public SetBookCommentEvent(long bookId, String comment) {
        super(Map.of("bookId", bookId, "comment", comment));
        this.bookId = bookId;
        this.comment = comment;
    }
}
