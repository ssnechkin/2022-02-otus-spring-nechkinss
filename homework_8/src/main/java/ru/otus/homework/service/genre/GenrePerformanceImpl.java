package ru.otus.homework.service.genre;

import org.springframework.stereotype.Service;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.service.io.IOServiceStreams;

@Service
public class GenrePerformanceImpl implements GenrePerformance {
    private final IOServiceStreams ioService;

    public GenrePerformanceImpl(IOServiceStreams ioService) {
        this.ioService = ioService;
    }

    @Override
    public void delete(String id) {
        ioService.outputString("Genre deleted. ID: " + id);
    }

    @Override
    public void add(String id) {
        ioService.outputString("Genre added. ID: " + id);
    }

    @Override
    public void total(long count) {
        ioService.outputString("Total genre: " + count);
    }

    @Override
    public void notFound(String id) {
        ioService.outputString("The genre was not found by ID: " + id);
    }

    @Override
    public void output(Genre genre) {
        ioService.outputString("Genre"
                + " ID: " + genre.getId()
                + " Name: " + genre.getName()
                + " Description: " + genre.getDescription()
        );
    }

    @Override
    public void outputSetDescription(String id, String description) {
        ioService.outputString("Genre description is set. ID: " + id
                + " Description: " + (description != null ? description : ""));
    }
}
