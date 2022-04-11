package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.service.io.IOServiceStreams;
import ru.otus.homework.repository.genre.GenreDao;
import ru.otus.homework.entity.Genre;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;
    private final IOServiceStreams ioService;

    public GenreServiceImpl(GenreDao genreDao, IOServiceStreams ioService) {
        this.genreDao = genreDao;
        this.ioService = ioService;
    }

    @Override
    @Transactional
    public void delete(long genreId) {
        if (getGenreOrOutPutNotFound(genreId) != null) {
            genreDao.delete(genreId);
            ioService.outputString("Genre deleted. ID: " + genreId);
        }
    }

    @Override
    @Transactional
    public void add(String genreName) {
        long id = genreDao.insert(new Genre(null, genreName, ""));
        ioService.outputString("Genre added. ID: " + id);
    }

    @Override
    @Transactional
    public void outputAll() {
        List<Genre> genres = genreDao.getAll();
        ioService.outputString("Total genres: " + genreDao.count());
        for (Genre genre : genres) {
            outputGenre(genre);
        }
    }

    @Override
    @Transactional
    public void setDescription(long genreId, String description) {
        Genre genre = getGenreOrOutPutNotFound(genreId);
        if (genre != null) {
            genre.setDescription(description);
            genreDao.update(genre);
            ioService.outputString("Genre description is set. ID: " + genreId + " Description: " + description);
        }
    }

    private Genre getGenreOrOutPutNotFound(long genreId) {
        List<Genre> genres = genreDao.getById(genreId);
        if (genres.size() == 0) {
            ioService.outputString("The genre was not found by ID: " + genreId);
            return null;
        }
        return genres.get(0);
    }

    private void outputGenre(Genre genre) {
        ioService.outputString("Genre"
                + " ID: " + genre.getId()
                + " Name: " + genre.getName()
                + " Description: " + genre.getDescription()
        );
    }
}
