package ru.otus.homework.dao.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс GenreDaoImpl")
@JdbcTest
@Import(GenreDaoImpl.class)
class GenreDaoImplTest {
    @Autowired
    private GenreDaoImpl genreDao;

    @DisplayName("Получить идентификатор")
    @Test
    void generateId() {
        assertTrue(genreDao.generateId() >= 1);
    }

    @DisplayName("Добавление")
    @Test
    void insert() {
        long id = genreDao.generateId();
        Genre genre = new Genre(id, "NameS", "");
        genreDao.insert(genre);
        List<Genre> genre1 = genreDao.getById(id);
        assertEquals(genre1.get(0).getName(), "NameS");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        long id = genreDao.generateId();
        Genre genre = new Genre(id, "NameW", "DS");
        genreDao.insert(genre);
        genre.setName("NameR");
        genre.setDescription("DS2");
        genreDao.update(genre);
        List<Genre> genre1 = genreDao.getById(id);
        assertEquals("NameR", genre1.get(0).getName());
        assertEquals(genre1.get(0).getDescription(), "DS2");
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        long id = genreDao.generateId();
        Genre genre1 = new Genre(id, "Name_delete1", "");
        genreDao.insert(genre1);

        id = genreDao.generateId();
        Genre genre2 = new Genre(id, "Name_delete2", "");
        genreDao.insert(genre2);

        genreDao.delete(id);

        boolean name1Del = true;
        boolean name2Del = true;
        for (Genre genre : genreDao.getAll()) {
            if (genre.getName().equals("Name_delete1")) {
                name1Del = false;
            }
            if (genre.getName().equals("Name_delete2")) {
                name2Del = false;
            }
        }
        assertFalse(name1Del);
        assertTrue(name2Del);
    }

    @DisplayName("Получение всех")
    @Test
    void getAll() {
        Genre genre = new Genre(genreDao.generateId(), "Name", "");
        genreDao.insert(genre);
        List<Genre> genres = genreDao.getAll();
        assertTrue(genres.size() >= 1);
    }

    @DisplayName("Получение одного")
    @Test
    void getById() {
        long id = genreDao.generateId();
        Genre genre = new Genre(id, "NameQ", "");
        genreDao.insert(genre);
        List<Genre> genre1 = genreDao.getById(id);
        assertTrue(genre1.size() > 0 && genre1.get(0).getName().equals("NameQ"));
    }

    @DisplayName("Количество")
    @Test
    void count() {
        Genre genre = new Genre(genreDao.generateId(), "Name", "");
        genreDao.insert(genre);
        assertTrue(genreDao.count() >= 1);
    }
}