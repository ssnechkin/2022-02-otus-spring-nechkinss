package ru.otus.homework.shell.event.genre;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class SetGenreDescriptionEvent extends ApplicationEvent {
    @Getter
    private final long genreId;
    @Getter
    private final String description;

    public SetGenreDescriptionEvent(long genreId, String description) {
        super(Map.of("genreId", genreId, "description", description));
        this.genreId = genreId;
        this.description = description;
    }
}
