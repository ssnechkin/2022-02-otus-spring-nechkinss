package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.config.IOServiceStreams;
import ru.otus.homework.dao.book.BookGenreDao;
import ru.otus.homework.dao.genre.GenreDao;
import ru.otus.homework.domain.Genre;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;
    private final BookGenreDao bookGenreDao;
    private final IOServiceStreams ioService;

    public GenreServiceImpl(GenreDao genreDao, BookGenreDao bookGenreDao, IOServiceStreams ioService) {
        this.genreDao = genreDao;
        this.bookGenreDao = bookGenreDao;
        this.ioService = ioService;
    }

    @Override
    public void delete(long genreId) {
        if (getGenreOrOutPutNotFound(genreId) != null) {
            genreDao.delete(genreId);
            bookGenreDao.deleteLinks(genreId);
            ioService.outputString("Genre deleted. ID: " + genreId);
        }
    }

    @Override
    public void add(String genreName) {
        long id = genreDao.generateId();
        genreDao.insert(new Genre(id, genreName, ""));
        ioService.outputString("Genre added. ID: " + id);
    }

    @Override
    public void outputAll() {
        List<Genre> genres = genreDao.getAll();
        ioService.outputString("Total genres: " + genreDao.count());
        for (Genre genre : genres) {
            outputGenre(genre);
        }
    }

    @Override
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
