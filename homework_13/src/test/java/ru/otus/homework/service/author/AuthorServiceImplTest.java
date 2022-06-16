package ru.otus.homework.service.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.service.author.AuthorServiceImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс AuthorServiceImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class AuthorServiceImplTest {

    @Autowired
    private AuthorServiceImpl authorService;

    @Test
    @DisplayName("Добавление автора")
    void add() {
        String id = UUID.randomUUID().toString();
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        assertEquals("name" + id, authorService.getById(author.getId()).getName());
        authorService.delete(author);
    }

    @Test
    @DisplayName("Получение автора")
    void getById() {
        String id = UUID.randomUUID().toString();
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        assertEquals("name" + id, authorService.getById(author.getId()).getName());
        authorService.delete(author);
    }

    @Test
    @DisplayName("Получение всех авторов")
    void getAll() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Author author1 = authorService.add("surname" + id1, "name" + id1, "patronymic" + id1);
        Author author2 = authorService.add("surname" + id2, "name" + id2, "patronymic" + id2);
        Author authorT1 = null;
        Author authorT2 = null;
        for (Author author : authorService.getAll()) {
            if (author.getName().equals("name" + id1)) {
                authorT1 = author;
            }
            if (author.getName().equals("name" + id2)) {
                authorT2 = author;
            }
            if (authorT1 != null && authorT2 != null) {
                break;
            }
        }
        assertNotNull(authorT1);
        assertNotNull(authorT2);
        authorService.delete(author1);
        authorService.delete(author2);
    }

    @Test
    @DisplayName("Редактирование автора")
    void edit() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        assertEquals("name" + id, authorService.getById(author.getId()).getName());
        authorService.edit(author, "surname" + id2, "name" + id2, "patronymic" + id2);
        assertEquals("name" + id2, authorService.getById(author.getId()).getName());
        authorService.delete(author);
    }

    @Test
    @DisplayName("Удаление автора")
    void delete() {
        String id = UUID.randomUUID().toString();
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        long authorId = author.getId();
        assertEquals("name" + id, authorService.getById(author.getId()).getName());
        authorService.delete(author);
        assertNull(authorService.getById(authorId));
    }
}