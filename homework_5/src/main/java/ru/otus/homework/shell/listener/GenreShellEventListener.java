package ru.otus.homework.shell.listener;

import org.springframework.stereotype.Component;
import ru.otus.homework.service.GenreService;
import ru.otus.homework.shell.event.genre.AddGenreEvent;
import ru.otus.homework.shell.event.genre.DeleteGenreByIdEvent;
import ru.otus.homework.shell.event.genre.OutputAllGenresEvent;
import ru.otus.homework.shell.event.genre.SetGenreDescriptionEvent;

@Component
public class GenreShellEventListener {
    private final GenreService genreService;

    public GenreShellEventListener(GenreService genreService) {
        this.genreService = genreService;
    }

    @org.springframework.context.event.EventListener
    public void addGenreEvent(AddGenreEvent event) {
        genreService.add(event.getGenreName());
    }

    @org.springframework.context.event.EventListener
    public void deleteGenreByIdEvent(DeleteGenreByIdEvent event) {
        genreService.delete(event.getGenreId());
    }

    @org.springframework.context.event.EventListener
    public void outputAllGenresEvent(OutputAllGenresEvent event) {
        genreService.outputAll();
    }

    @org.springframework.context.event.EventListener
    public void setGenreDescriptionEvent(SetGenreDescriptionEvent event) {
        genreService.setDescription(event.getGenreId(), event.getDescription());
    }
}