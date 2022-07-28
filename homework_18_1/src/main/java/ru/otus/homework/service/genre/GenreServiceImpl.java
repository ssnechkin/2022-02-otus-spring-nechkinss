package ru.otus.homework.service.genre;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.repository.book.BookRepository;
import ru.otus.homework.repository.genre.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public GenreServiceImpl(GenreRepository genreRepository, BookRepository bookRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Genre add(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Override
    public Genre getById(long id) {
        Optional<Genre> genre = genreRepository.findById(id);
        return genre.orElse(null);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre editDescription(Genre genre, String description) {
        genre.setDescription(description);
        return genreRepository.save(genre);
    }

    @Override
    public Genre editName(Genre genre, String genreName) {
        genre.setName(genreName);
        return genreRepository.save(genre);
    }

    @Override
    public boolean delete(Genre genre) {
        if (genre.getBooks().size() > 0) {
            return false;
        } else {
            genreRepository.delete(genre);
            return true;
        }
    }
}
