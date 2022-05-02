package ru.otus.homework.shell.listener;

import org.springframework.stereotype.Component;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.performance.genre.GenrePerformance;
import ru.otus.homework.shell.event.genre.*;

@Component
public class GenreShellEventListener {

    private final GenrePerformance genrePerformance;
    private final IOService ioService;

    public GenreShellEventListener(GenrePerformance genrePerformance, IOService ioService) {
        this.genrePerformance = genrePerformance;
        this.ioService = ioService;
    }

    @org.springframework.context.event.EventListener
    public void addGenreEvent(AddGenreEvent event) {
        ioService.outputString(genrePerformance.add(event.getGenreName()));
    }

    @org.springframework.context.event.EventListener
    public void outputAllGenresEvent(OutputAllGenresEvent event) {
        for (String line : genrePerformance.getAll()) {
            ioService.outputString(line);
        }
    }

    @org.springframework.context.event.EventListener
    public void setGenreDescriptionEvent(SetGenreDescriptionEvent event) {
        ioService.outputString(genrePerformance.editDescription(event.getGenreId(), event.getDescription()));
    }

    @org.springframework.context.event.EventListener
    public void editGenreEvent(EditGenreEvent event) {
        ioService.outputString(genrePerformance.edit(event.getGenreId(), event.getGenreName()));
    }

    @org.springframework.context.event.EventListener
    public void deleteGenreByIdEvent(DeleteGenreByIdEvent event) {
        ioService.outputString(genrePerformance.deleteById(event.getGenreId()));
    }
}