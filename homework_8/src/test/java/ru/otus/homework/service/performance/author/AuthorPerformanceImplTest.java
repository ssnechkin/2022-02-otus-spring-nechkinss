package ru.otus.homework.service.performance.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.domain.Author;
import ru.otus.homework.repository.author.AuthorRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Класс AuthorPerformanceImpl")
@DataMongoTest
@ComponentScan("ru.otus.homework")
class AuthorPerformanceImplTest {

    @Autowired
    private AuthorPerformanceImpl authorPerformance;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Добавление автора")
    void addAuthor() {
        String id = UUID.randomUUID().toString();
        authorPerformance.add("surname" + id, "name" + id, "patronymic" + id);
        Author authorT = null;
        for (Author author : authorRepository.findAll()) {
            if (author != null
                    && author.getName() != null
                    && author.getName().equals("name" + id)) {
                authorT = author;
                break;
            }
        }
        assertNotNull(authorT);
        authorRepository.delete(authorT);
    }

    @Test
    @DisplayName("Получение всех авторов")
    void authors() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        authorPerformance.add("surname" + id, "name" + id, "patronymic" + id);
        authorPerformance.add("surname" + id2, "name" + id2, "patronymic" + id2);
        String authorT1 = null;
        String authorT2 = null;
        for (String line : authorPerformance.getAll()) {
            if (line != null) {
                if (line.contains("name" + id)) {
                    authorT1 = line;
                }
                if (line.contains("name" + id2)) {
                    authorT2 = line;
                }
                if (authorT1 != null && authorT2 != null) {
                    break;
                }
            }
        }
        assertNotNull(authorT1);
        assertNotNull(authorT2);

        Author author1 = null;
        Author author2 = null;
        for (Author author : authorRepository.findAll()) {
            if (author != null && author.getName() != null) {
                if (author.getName().equals("name" + id)) {
                    author1 = author;
                }
                if (author.getName().equals("name" + id2)) {
                    author2 = author;
                }
                if (author1 != null && author2 != null) {
                    break;
                }
            }
        }
        if (author1 != null)
            authorRepository.delete(author1);
        if (author2 != null)
            authorRepository.delete(author2);
    }

    @Test
    @DisplayName("Редактирование автора")
    void editAuthor() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        authorPerformance.add("surname" + id, "name" + id, "patronymic" + id);
        Author authorT = null;
        for (Author author : authorRepository.findAll()) {
            if (author != null
                    && author.getName() != null
                    && author.getName().equals("name" + id)) {
                authorT = author;
                break;
            }
        }
        assertNotNull(authorT);

        authorPerformance.edit(authorT.getId(), "surname" + id2, "name" + id2, "patronymic" + id2);
        authorT = null;
        for (Author author : authorRepository.findAll()) {
            if (author != null
                    && author.getName() != null
                    && author.getName().equals("name" + id2)) {
                authorT = author;
                break;
            }
        }
        assertNotNull(authorT);
        authorRepository.delete(authorT);
    }

    @Test
    @DisplayName("Удаление автора")
    void deleteAuthorById() {
        String id = UUID.randomUUID().toString();
        authorPerformance.add("surname" + id, "name" + id, "patronymic" + id);
        Author authorT = null;
        for (Author author : authorRepository.findAll()) {
            if (author != null
                    && author.getName() != null
                    && author.getName().equals("name" + id)) {
                authorT = author;
                break;
            }
        }
        assertNotNull(authorT);
        String delId = authorT.getId();
        authorPerformance.deleteById(delId);
        assertFalse(authorRepository.findById(delId).isPresent());
    }
}