package ru.otus.homework.service.repository.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.service.genre.GenreService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс GenreServiceImpl")
@DataMongoTest
@ComponentScan("ru.otus.homework")
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @Test
    @DisplayName("Добавление жанра")
    void add() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        assertEquals("name" + id, genreService.getById(genre.getId()).getName());
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Получение жанра")
    void getById() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        assertEquals("name" + id, genreService.getById(genre.getId()).getName());
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Получение всех жанров")
    void getAll() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Genre genre1 = genreService.add("name" + id1);
        Genre genre2 = genreService.add("name" + id2);
        Genre genreT1 = null;
        Genre genreT2 = null;
        for (Genre genre : genreService.getAll()) {
            if (genre.getName().equals("name" + id1)) {
                genreT1 = genre;
            }
            if (genre.getName().equals("name" + id2)) {
                genreT2 = genre;
            }
            if (genreT1 != null && genreT2 != null) {
                break;
            }
        }
        assertNotNull(genreT1);
        assertNotNull(genreT2);
        genreService.delete(genre1);
        genreService.delete(genre2);
    }

    @Test
    @DisplayName("Редактирование описания жанра")
    void editDescription() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        genreService.editDescription(genre, "description" + id);
        assertEquals("description" + id, genreService.getById(genre.getId()).getDescription());
        genreService.editDescription(genre, "description" + id2);
        assertEquals("description" + id2, genreService.getById(genre.getId()).getDescription());
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Редактирование наименования жанра")
    void editName() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        assertEquals("name" + id, genreService.getById(genre.getId()).getName());
        genreService.editName(genre, "name" + id2);
        assertEquals("name" + id2, genreService.getById(genre.getId()).getName());
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Удаление жанра")
    void delete() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        assertEquals("name" + id, genreService.getById(genre.getId()).getName());
        String genreId = genre.getId();
        genreService.delete(genre);
        assertNull(genreService.getById(genreId));
    }
}