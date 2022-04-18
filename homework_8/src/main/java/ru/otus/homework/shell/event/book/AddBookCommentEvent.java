package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class AddBookCommentEvent extends ApplicationEvent {

    @Getter
    private final String bookId;

    @Getter
    private final String comment;

    public AddBookCommentEvent(String bookId, String comment) {
        super(Map.of("bookId", bookId, "comment", comment));
        this.bookId = bookId;
        this.comment = comment;
    }
}
