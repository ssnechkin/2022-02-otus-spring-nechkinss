package ru.otus.homework.shell.event.book;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class RemoveTheAuthorFromTheBookEvent extends ApplicationEvent {

    @Getter
    private final String bookId, authorId;

    public RemoveTheAuthorFromTheBookEvent(String bookId, String authorId) {
        super(Map.of("bookId", bookId, "authorId", authorId));
        this.bookId = bookId;
        this.authorId = authorId;
    }
}
