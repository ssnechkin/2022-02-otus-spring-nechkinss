package ru.otus.homework.service.performance.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.genre.GenreRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Класс GenrePerformanceImpl")
@DataMongoTest
@ComponentScan("ru.otus.homework")
class GenrePerformanceImplTest {

    @Autowired
    private GenrePerformanceImpl genrePerformance;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("Добавление жанра")
    void addGenre() {
        String id = UUID.randomUUID().toString();
        genrePerformance.add("name" + id);
        Genre genreT = null;
        for (Genre genre : genreRepository.findAll()) {
            if (genre != null
                    && genre.getName() != null
                    && genre.getName().equals("name" + id)) {
                genreT = genre;
                break;
            }
        }
        assertNotNull(genreT);
        genreRepository.delete(genreT);
    }

    @Test
    @DisplayName("Получение всех жанров")
    void genres() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        genrePerformance.add("name" + id);
        genrePerformance.add("name" + id2);
        String genreT1 = null;
        String genreT2 = null;
        for (String line : genrePerformance.getAll()) {
            if (line != null) {
                if (line.contains("name" + id)) {
                    genreT1 = line;
                }
                if (line.contains("name" + id2)) {
                    genreT2 = line;
                }
                if (genreT1 != null && genreT2 != null) {
                    break;
                }
            }
        }
        assertNotNull(genreT1);
        assertNotNull(genreT2);

        Genre genre1 = null;
        Genre genre2 = null;
        for (Genre genre : genreRepository.findAll()) {
            if (genre != null && genre.getName() != null) {
                if (genre.getName().equals("name" + id)) {
                    genre1 = genre;
                }
                if (genre.getName().equals("name" + id2)) {
                    genre2 = genre;
                }
                if (genre1 != null && genre2 != null) {
                    break;
                }
            }
        }
        if (genre1 != null)
            genreRepository.delete(genre1);
        if (genre2 != null)
            genreRepository.delete(genre2);
    }

    @Test
    @DisplayName("Редакрирование описания жанра")
    void editGenreDescription() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        genrePerformance.add("name" + id);
        Genre genreT = null;
        for (Genre genre : genreRepository.findAll()) {
            if (genre != null
                    && genre.getName() != null
                    && genre.getName().equals("name" + id)) {
                genreT = genre;
                break;
            }
        }
        assertNotNull(genreT);

        genrePerformance.editDescription(genreT.getId(), "description" + id2);
        genreT = null;
        for (Genre genre : genreRepository.findAll()) {
            if (genre != null
                    && genre.getDescription() != null
                    && genre.getDescription().equals("description" + id2)) {
                genreT = genre;
                break;
            }
        }
        assertNotNull(genreT);
        genreRepository.delete(genreT);
    }

    @Test
    @DisplayName("Редактирование жанра")
    void editGenre() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        genrePerformance.add("name" + id);
        Genre genreT = null;
        for (Genre genre : genreRepository.findAll()) {
            if (genre != null
                    && genre.getName() != null
                    && genre.getName().equals("name" + id)) {
                genreT = genre;
                break;
            }
        }
        assertNotNull(genreT);

        genrePerformance.edit(genreT.getId(), "name" + id2);
        genreT = null;
        for (Genre genre : genreRepository.findAll()) {
            if (genre != null
                    && genre.getName() != null
                    && genre.getName().equals("name" + id2)) {
                genreT = genre;
                break;
            }
        }
        assertNotNull(genreT);
        genreRepository.delete(genreT);
    }

    @Test
    @DisplayName("Удаление жанра")
    void deleteGenreById() {
        String id = UUID.randomUUID().toString();
        genrePerformance.add("name" + id);
        Genre genreT = null;
        for (Genre genre : genreRepository.findAll()) {
            if (genre != null
                    && genre.getName() != null
                    && genre.getName().equals("name" + id)) {
                genreT = genre;
                break;
            }
        }
        assertNotNull(genreT);
        String delId = genreT.getId();
        genrePerformance.deleteById(delId);
        assertFalse(genreRepository.findById(delId).isPresent());
    }
}