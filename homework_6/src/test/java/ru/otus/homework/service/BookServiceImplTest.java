package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.otus.homework.repository.author.AuthorDaoImpl;
import ru.otus.homework.repository.book.BookCommentDaoImpl;
import ru.otus.homework.repository.book.BookDaoImpl;
import ru.otus.homework.repository.genre.GenreDaoImpl;
import ru.otus.homework.service.io.IOServiceStreams;

import java.util.UUID;

@DisplayName("Класс BookServiceImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private GenreServiceImpl genreService;

    @Test
    void add() {
        bookService.add("Name");
    }

    @Test
    void addAuthor() {
        bookService.add("Name");
        authorService.add("Surname", "NameY", "Patronymic");
        bookService.addAuthor(1, 1);
    }

    @Test
    void addGenre() {
        bookService.add("Name");
        genreService.add("NameS");
        bookService.addGenre(1, 1);
    }

    @Test
    void addComment() {
        bookService.add("Name1");
        bookService.addComment(1, "comment");
    }

    @Test
    void delete() {
        bookService.add("Name1");
        bookService.add("Name2");
        bookService.add("Name3");
        bookService.delete(3);
    }

    @Test
    void outputAll() {
        bookService.outputAll();
    }

    @Test
    void output() {
        bookService.add("Name1");
        bookService.output(1);
    }

    @Test
    void removeAuthor() {
        bookService.add("Name1");
        bookService.add("Name2");
        bookService.add("Name3");
        bookService.add("Name4");
        bookService.add("Name5");
        authorService.add("Surname", "NameY", "Patronymic");
        bookService.addAuthor(5, 1);
        bookService.removeAuthor(5, 1);
    }

    @Test
    void removeGenre() {
        bookService.add("Name1");
        bookService.add("Name2");
        genreService.add("NameS");
        bookService.addGenre(2, 1);
        bookService.removeGenre(2, 1);
    }

    @Test
    void updateComment() {
        bookService.add("Name1");
        bookService.addComment(1, "comment1");
        bookService.updateBookComment(1, "comment2");
    }

    @Test
    void updateName() {
        bookService.add("Name1");
        bookService.updateBookName(1, "Name2");
    }

    @Test
    void removeComment() {
        String comment = UUID.randomUUID().toString();
        bookService.add("Name1");
        bookService.addComment(1, comment);
        bookService.removeBookComment(1);
    }
}