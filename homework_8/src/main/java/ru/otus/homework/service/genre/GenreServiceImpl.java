package ru.otus.homework.service.genre;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.repository.book.BookDao;
import ru.otus.homework.repository.genre.GenreDao;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;
    private final BookDao bookDao;
    private final GenrePerformance genrePerformance;

    public GenreServiceImpl(GenreDao genreDao, BookDao bookDao, GenrePerformance genrePerformance) {
        this.genreDao = genreDao;
        this.bookDao = bookDao;
        this.genrePerformance = genrePerformance;
    }

    @Transactional
    @Override
    public void delete(String authorId) {
        Genre genre = genreDao.getById(authorId);
        if (genre != null) {
            for (Book book : bookDao.getAll()) {
                book.getGenres().remove(genre);
                bookDao.update(book);
            }
            genreDao.delete(authorId);
            genrePerformance.delete(authorId);
        } else {
            genrePerformance.notFound(authorId);
        }
    }

    @Transactional
    @Override
    public void add(String genreName) {
        Genre genre = new Genre();
        genre.setName(genreName);
        String id = genreDao.insert(genre).getId();
        genrePerformance.add(id);
    }

    @Transactional(readOnly = true)
    @Override
    public void outputAll() {
        List<Genre> genres = genreDao.getAll();
        genrePerformance.total(genres.size());
        for (Genre genre : genres) {
            genrePerformance.output(genre);
        }
    }

    @Transactional
    @Override
    public void setDescription(String genreId, String description) {
        Genre genre = genreDao.getById(genreId);
        if (genre != null) {
            genre.setDescription(description);
            genreDao.update(genre);
            genrePerformance.outputSetDescription(genreId, description);
        } else {
            genrePerformance.notFound(genreId);
        }
    }
}
