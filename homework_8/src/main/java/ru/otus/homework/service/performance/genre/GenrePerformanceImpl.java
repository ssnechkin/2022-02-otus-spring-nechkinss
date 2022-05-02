package ru.otus.homework.service.performance.genre;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.service.texter.genre.GenreTexter;
import ru.otus.homework.service.genre.GenreService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenrePerformanceImpl implements GenrePerformance {

    private final GenreService genreService;
    private final GenreTexter genreTexter;

    public GenrePerformanceImpl(GenreService genreService, GenreTexter genreTexter) {
        this.genreService = genreService;
        this.genreTexter = genreTexter;
    }

    @Override
    public String add(String name) {
        Genre genre = genreService.add(name);
        return genreTexter.add(genre);
    }

    @Override
    public List<String> getAll() {
        List<Genre> genreList = genreService.getAll();
        ArrayList<String> result = new ArrayList<>();
        result.add(genreTexter.total(genreList.size()));
        for (Genre genre : genreList) {
            result.add(genreTexter.toString(genre));
        }
        return result;
    }

    @Override
    public String editDescription(String id, String description) {
        Genre genre = genreService.getById(id);
        if (genre == null) {
            return genreTexter.notFound(id);
        } else {
            genre = genreService.editDescription(genre, description);
            return genreTexter.edit(genre);
        }
    }

    @Override
    public String edit(String id, String name) {
        Genre genre = genreService.getById(id);
        if (genre == null) {
            return genreTexter.notFound(id);
        } else {
            genre = genreService.editName(genre, name);
            return genreTexter.edit(genre);
        }
    }

    @Override
    public String deleteById(String id) {
        Genre genre = genreService.getById(id);
        if (genre == null) {
            return genreTexter.notFound(id);
        } else {
            genreService.delete(genre);
            return genreTexter.delete(id);
        }
    }
}