package ru.otus.homework.repository.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.entity.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс AuthorDaoImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class AuthorDaoImplTest {

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("Добавление")
    @Test
    void insert() {
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        long id = authorRepository.save(author).getId();
        Author author1 = authorRepository.getById(id);
        assertEquals(author1.getName(), "NameY");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameV");
        author.setPatronymic("Patronymic");
        author = authorRepository.save(author);
        author.setName("NameF");
        authorRepository.save(author);
        Author author1 = authorRepository.getById(author.getId());
        assertEquals("NameF", author1.getName());
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        Author author1 = new Author();
        author1.setSurname("Surname");
        author1.setName("Name_delete1");
        author1.setPatronymic("Patronymic");
        authorRepository.save(author1);
        Author author2 = new Author();
        author2.setSurname("Surname");
        author2.setName("Name_delete2");
        author2.setPatronymic("Patronymic");
        author2 = authorRepository.save(author2);

        authorRepository.delete(author2);

        boolean name1Del = true;
        boolean name2Del = true;
        for (Author author : authorRepository.findAll()) {
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
        authorRepository.save(author);
        List<Author> authors = authorRepository.findAll();
        assertTrue(authors.size() >= 1);
    }

    @DisplayName("Получение одного")
    @Test
    void getById() {
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameM");
        author.setPatronymic("Patronymic");
        author = authorRepository.save(author);
        Author author1 = authorRepository.getById(author.getId());
        assertEquals("NameM", author1.getName());
    }
}