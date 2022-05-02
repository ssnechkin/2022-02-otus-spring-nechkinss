package ru.otus.homework.shell.event.author;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeleteAuthorByIdEvent extends ApplicationEvent {

    @Getter
    private final String authorId;

    public DeleteAuthorByIdEvent(String authorId) {
        super(authorId);
        this.authorId = authorId;
    }
}
