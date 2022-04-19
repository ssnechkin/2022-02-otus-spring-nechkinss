package ru.otus.homework.shell.event.genre;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class EditGenreEvent extends ApplicationEvent {

    @Getter
    private final long genreId;

    @Getter
    private final String genreName;

    public EditGenreEvent(long genreId, String genreName) {
        super(Map.of("genreId", genreId, "genreName", genreName));
        this.genreId = genreId;
        this.genreName = genreName;
    }
}
