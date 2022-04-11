package ru.otus.homework.shell.event.author;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeleteAuthorByIdEvent extends ApplicationEvent {
    @Getter
    private final long authorId;

    public DeleteAuthorByIdEvent(long authorId) {
        super(authorId);
        this.authorId = authorId;
    }
}
