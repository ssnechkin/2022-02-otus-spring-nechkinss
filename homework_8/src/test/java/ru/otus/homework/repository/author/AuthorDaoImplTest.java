package ru.otus.homework.repository.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.entity.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс AuthorDaoImpl")
@DataMongoTest
@ComponentScan("ru.otus.homework")
class AuthorDaoImplTest {

    @Autowired
    private AuthorDaoImpl authorDao;

    @DisplayName("Добавление")
    @Test
    void insert() {
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        author = authorDao.insert(author);
        Author author1 = authorDao.getById(author.getId());
        assertEquals(author1.getName(), "NameY");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameV");
        author.setPatronymic("Patronymic");
        author = authorDao.insert(author);
        author.setName("NameF");
        authorDao.update(author);
        Author author1 = authorDao.getById(author.getId());
        assertEquals("NameF", author1.getName());
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        Author author1 = new Author();
        author1.setSurname("Surname");
        author1.setName("Name_delete1");
        author1.setPatronymic("Patronymic");
        author1 = authorDao.insert(author1);

        Author author2 = new Author();
        author2.setSurname("Surname");
        author2.setName("Name_delete2");
        author2.setPatronymic("Patronymic");
        author2 = authorDao.insert(author2);

        authorDao.delete(author2.getId());

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
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameZ");
        author.setPatronymic("Patronymic");
        authorDao.insert(author);
        List<Author> authors = authorDao.getAll();
        assertTrue(authors.size() >= 1);
    }

    @DisplayName("Получение одного")
    @Test
    void getById() {
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameM");
        author.setPatronymic("Patronymic");
        author = authorDao.insert(author);
        Author author1 = authorDao.getById(author.getId());
        assertEquals("NameM", author1.getName());
    }
}