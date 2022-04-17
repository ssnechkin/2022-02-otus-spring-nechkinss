package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.repository.genre.GenreDaoImpl;

import java.util.UUID;

@DisplayName("Класс GenreServiceImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class GenreServiceImplTest {

    @Autowired
    private GenreServiceImpl genreService;

    @Autowired
    private GenreDaoImpl genreDao;

    @Test
    void delete() {
        Genre genre = new Genre();
        genre.setName(UUID.randomUUID().toString());
        genre = genreDao.insert(genre);
        genreService.delete(genre.getId());
    }

    @Test
    void add() {
        genreService.add("Name");
    }

    @Test
    void outputAll() {
        genreService.outputAll();
    }

    @Test
    void setDescription() {
        Genre genre = new Genre();
        genre.setName(UUID.randomUUID().toString());
        genre = genreDao.insert(genre);
        genreService.setDescription(genre.getId(), "description");
    }
}