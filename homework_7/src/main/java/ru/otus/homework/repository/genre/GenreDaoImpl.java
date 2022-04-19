package ru.otus.homework.repository.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.homework.entity.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final GenreRepository genreRepository;

    @Override
    public void delete(long id) {
        genreRepository.findById(id).ifPresent(genreRepository::delete);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre getById(long id) {
        return genreRepository.getById(id);
    }

    @Override
    public Genre insert(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public void update(Genre genre) {
        genreRepository.findById(genre.getId()).ifPresent(genreRepository::save);
    }
}