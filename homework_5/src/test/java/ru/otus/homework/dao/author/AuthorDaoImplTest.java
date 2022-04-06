package ru.otus.homework.dao.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс AuthorDaoImpl")
@JdbcTest
@Import(AuthorDaoImpl.class)
class AuthorDaoImplTest {
    @Autowired
    private AuthorDaoImpl authorDao;

    @DisplayName("Получить идентификатор")
    @Test
    void generateId() {
        assertTrue(authorDao.generateId() >= 1);
    }

    @DisplayName("Добавление")
    @Test
    void insert() {
        long id = authorDao.generateId();
        Author author = new Author(id, "Surname", "NameY", "Patronymic");
        authorDao.insert(author);
        List<Author> author1 = authorDao.getById(id);
        assertEquals(author1.get(0).getName(), "NameY");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        long id = authorDao.generateId();
        Author author = new Author(id, "Surname", "NameV", "Patronymic");
        authorDao.insert(author);
        author.setName("NameF");
        authorDao.update(author);
        List<Author> author1 = authorDao.getById(id);
        assertEquals("NameF", author1.get(0).getName());
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        long id = authorDao.generateId();
        Author author1 = new Author(id, "Surname", "Name_delete1", "Patronymic");
        authorDao.insert(author1);

        id = authorDao.generateId();
        Author author2 = new Author(id, "Surname", "Name_delete2", "Patronymic");
        authorDao.insert(author2);

        authorDao.delete(id);

        boolean name1Del = true;
        boolean name2Del = true;
        for (Author author : authorDao.getAll()) {
            if (author.getName().equals("Name_delete1")) {
                name1Del = false;
            }
            if (author.getName().equals("Name_delete2")) {
                name2Del = false;
            }
        }
        assertFalse(name1Del);
        assertTrue(name2Del);
    }

    @DisplayName("Получение всех")
    @Test
    void getAll() {
        long id = authorDao.generateId();
        Author author = new Author(id, "Surname", "NameZ", "Patronymic");
        authorDao.insert(author);
        List<Author> authors = authorDao.getAll();
        assertTrue(authors.size() >= 1);
    }

    @DisplayName("Получение одного")
    @Test
    void getById() {
        long id = authorDao.generateId();
        Author author = new Author(id, "Surname", "NameM", "Patronymic");
        authorDao.insert(author);
        List<Author> author1 = authorDao.getById(id);
        assertTrue(author1.size() > 0 && author1.get(0).getName().equals("NameM"));
    }

    @DisplayName("Количество")
    @Test
    void count() {
        Author author = new Author(authorDao.generateId(), "Surname", "NameC", "Patronymic");
        authorDao.insert(author);
        assertTrue(authorDao.count() >= 1);
    }
}