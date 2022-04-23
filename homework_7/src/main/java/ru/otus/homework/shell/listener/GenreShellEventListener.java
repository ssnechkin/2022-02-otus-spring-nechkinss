package ru.otus.homework.shell.listener;

import org.springframework.stereotype.Component;
import ru.otus.homework.service.genre.GenreService;
import ru.otus.homework.shell.event.genre.*;

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
    public void editGenreEvent(EditGenreEvent event) {
        genreService.edit(event.getGenreId(), event.getGenreName());
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