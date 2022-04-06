package ru.otus.homework.shell.event.genre;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class AddGenreEvent extends ApplicationEvent {
    @Getter
    private final String genreName;

    public AddGenreEvent(String genreName) {
        super(genreName);
        this.genreName = genreName;
    }
}
