package ru.otus.homework.repository.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.entity.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс GenreDaoImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class GenreDaoImplTest {

    @Autowired
    private GenreDaoImpl genreDao;

    @DisplayName("Добавление")
    @Test
    void insert() {
        Genre genre = new Genre();
        genre.setName("NameS");
        long id = genreDao.insert(genre).getId();
        Genre genre1 = genreDao.getById(id);
        assertEquals(genre1.getName(), "NameS");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Genre genre = new Genre();
        genre.setName("NameW");
        genre.setDescription("DS");
        long id = genreDao.insert(genre).getId();
        genre.setName("NameR");
        genre.setDescription("DS2");
        genreDao.update(genre);
        Genre genre1 = genreDao.getById(id);
        assertEquals("NameR", genre1.getName());
        assertEquals(genre1.getDescription(), "DS2");
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        Genre genre1 = new Genre();
        genre1.setName("Name_delete1");
        genreDao.insert(genre1);
        Genre genre2 = new Genre();
        genre2.setName("Name_delete2");
        long id = genreDao.insert(genre2).getId();

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
        Genre genre = new Genre();
        genre.setName("Name");
        genreDao.insert(genre);
        List<Genre> genres = genreDao.getAll();
        assertTrue(genres.size() >= 1);
    }

    @DisplayName("Получение одного")
    @Test
    void getById() {
        Genre genre = new Genre();
        genre.setName("NameQ");
        long id = genreDao.insert(genre).getId();
        Genre genre1 = genreDao.getById(id);
        assertEquals("NameQ", genre1.getName());
    }
}