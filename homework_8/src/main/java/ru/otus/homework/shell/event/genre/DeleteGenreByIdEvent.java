package ru.otus.homework.shell.event.genre;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeleteGenreByIdEvent extends ApplicationEvent {

    @Getter
    private final String genreId;

    public DeleteGenreByIdEvent(String genreId) {
        super(genreId);
        this.genreId = genreId;
    }
}
