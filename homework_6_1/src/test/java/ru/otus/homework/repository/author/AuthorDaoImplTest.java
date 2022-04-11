package ru.otus.homework.repository.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.entity.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс AuthorDaoImpl")
@DataJpaTest
@Import(AuthorDaoImpl.class)
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
        long id = authorDao.insert(author);
        List<Author> author1 = authorDao.getById(id);
        assertEquals(author1.get(0).getName(), "NameY");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameV");
        author.setPatronymic("Patronymic");
        long id = authorDao.insert(author);
        author.setName("NameF");
        authorDao.update(author);
        List<Author> author1 = authorDao.getById(id);
        assertEquals("NameF", author1.get(0).getName());
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        Author author1 = new Author();
        author1.setSurname("Surname");
        author1.setName("Name_delete1");
        author1.setPatronymic("Patronymic");
        authorDao.insert(author1);
        Author author2 = new Author();
        author2.setSurname("Surname");
        author2.setName("Name_delete2");
        author2.setPatronymic("Patronymic");
        long id = authorDao.insert(author2);

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
        long id = authorDao.insert(author);
        List<Author> author1 = authorDao.getById(id);
        assertTrue(author1.size() > 0 && author1.get(0).getName().equals("NameM"));
    }

    @DisplayName("Количество")
    @Test
    void count() {
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameC");
        author.setPatronymic("Patronymic");
        authorDao.insert(author);
        assertTrue(authorDao.count() >= 1);
    }
}