package ru.otus.homework.service.genre;

import org.springframework.stereotype.Service;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.repository.book.BookRepository;
import ru.otus.homework.repository.genre.GenreRepository;
import ru.otus.homework.service.performance.genre.GenrePerformance;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final GenrePerformance genrePerformance;

    public GenreServiceImpl(GenreRepository genreRepository, BookRepository bookRepository, GenrePerformance genrePerformance) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.genrePerformance = genrePerformance;
    }

    @Override
    public void delete(long genreId) {
        Optional<Genre> genre = genreRepository.findById(genreId);
        if (genre.isPresent()) {
            if (genre.get().getBooks() != null) {
                for (Book book : bookRepository.findAll()) {
                    if (book.getGenres().contains(genre.get())) {
                        book.getGenres().remove(genre.get());
                        bookRepository.save(book);
                    }
                }
            }
            genreRepository.delete(genre.get());
            genrePerformance.delete(genreId);
        } else {
            genrePerformance.notFound(genreId);
        }
    }

    @Override
    public void add(String genreName) {
        Genre genre = new Genre();
        genre.setName(genreName);
        long id = genreRepository.save(genre).getId();
        genrePerformance.add(id);
    }

    @Override
    public void outputAll() {
        List<Genre> genres = genreRepository.findAll();
        genrePerformance.total(genres.size());
        for (Genre genre : genres) {
            genrePerformance.output(genre);
        }
    }

    @Override
    public void setDescription(long genreId, String description) {
        Optional<Genre> genre = genreRepository.findById(genreId);
        if (genre.isPresent()) {
            genre.get().setDescription(description);
            genreRepository.save(genre.get());
            genrePerformance.outputSetDescription(genreId, description);
        } else {
            genrePerformance.notFound(genreId);
        }
    }
}
