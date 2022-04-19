package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.entity.Author;
import ru.otus.homework.repository.author.AuthorDaoImpl;

@DisplayName("Класс AuthorServiceImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class AuthorServiceImplTest {

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private AuthorDaoImpl authorDao;

    @DisplayName("Удаление")
    @Test
    void delete() {
        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        author = authorDao.insert(author);
        authorService.delete(author.getId());
    }

    @DisplayName("Добавление")
    @Test
    void add() {
        authorService.add("surname", "name", "patronymic");
    }

    @DisplayName("Вывод всех")
    @Test
    void outputAll() {
        authorService.outputAll();
    }
}