package ru.otus.homework.repository.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.entity.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс GenreDaoImpl")
@DataJpaTest
@Import(GenreDaoImpl.class)
class GenreDaoImplTest {
    @Autowired
    private GenreDaoImpl genreDao;

    @DisplayName("Добавление")
    @Test
    void insert() {
        Genre genre = new Genre();
        genre.setName("NameS");
        long id = genreDao.insert(genre);
        List<Genre> genre1 = genreDao.getById(id);
        assertEquals(genre1.get(0).getName(), "NameS");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Genre genre = new Genre();
        genre.setName("NameW");
        genre.setDescription("DS");
        long id = genreDao.insert(genre);
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
        Genre genre1 = new Genre();
        genre1.setName("Name_delete1");
        genreDao.insert(genre1);
        Genre genre2 = new Genre();
        genre2.setName("Name_delete2");
        long id = genreDao.insert(genre2);

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
        long id = genreDao.insert(genre);
        List<Genre> genre1 = genreDao.getById(id);
        assertTrue(genre1.size() > 0 && genre1.get(0).getName().equals("NameQ"));
    }

    @DisplayName("Количество")
    @Test
    void count() {
        Genre genre = new Genre();
        genre.setName("Name");
        genreDao.insert(genre);
        assertTrue(genreDao.count() >= 1);
    }
}