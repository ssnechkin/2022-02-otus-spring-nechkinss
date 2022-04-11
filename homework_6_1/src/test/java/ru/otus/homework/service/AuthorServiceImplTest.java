package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.repository.author.AuthorDaoImpl;
import ru.otus.homework.service.io.IOServiceStreams;

@DisplayName("Класс AuthorServiceImpl")
@DataJpaTest
@Import({AuthorServiceImpl.class, AuthorDaoImpl.class, IOServiceStreams.class})
class AuthorServiceImplTest {
    @Autowired
    private AuthorServiceImpl authorService;

    @DisplayName("Удаление")
    @Test
    void delete() {
        authorService.add("surname", "name", "patronymic");
        authorService.delete(1);
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