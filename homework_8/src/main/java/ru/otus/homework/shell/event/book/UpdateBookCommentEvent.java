package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class UpdateBookCommentEvent extends ApplicationEvent {

    @Getter
    private final String bookCommentId;

    @Getter
    private final String comment;

    public UpdateBookCommentEvent(String bookCommentId, String comment) {
        super(Map.of("bookCommentId", bookCommentId, "comment", comment));
        this.bookCommentId = bookCommentId;
        this.comment = comment;
    }
}
