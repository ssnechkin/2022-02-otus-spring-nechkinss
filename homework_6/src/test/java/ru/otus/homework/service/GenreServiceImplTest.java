package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.repository.genre.GenreDaoImpl;
import ru.otus.homework.service.io.IOServiceStreams;

@DisplayName("Класс GenreServiceImpl")
@DataJpaTest
@Import({GenreServiceImpl.class, GenreDaoImpl.class, IOServiceStreams.class})
class GenreServiceImplTest {

    @Autowired
    private GenreServiceImpl genreService;

    @Test
    void delete() {
        genreService.add("Name");
        genreService.delete(1);
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
        genreService.add("Name");
        genreService.setDescription(1, "description");
    }
}