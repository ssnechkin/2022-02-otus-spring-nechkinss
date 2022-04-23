package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class RemoveBookCommentEvent extends ApplicationEvent {

    @Getter
    private final long bookCommentId;

    public RemoveBookCommentEvent(long bookCommentId) {
        super(Map.of("bookCommentId", bookCommentId));
        this.bookCommentId = bookCommentId;
    }
}
