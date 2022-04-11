package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.repository.author.AuthorDaoImpl;
import ru.otus.homework.repository.book.BookDaoImpl;
import ru.otus.homework.repository.genre.GenreDaoImpl;
import ru.otus.homework.service.io.IOServiceStreams;

@DisplayName("Класс BookServiceImpl")
@DataJpaTest
@Import({BookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, AuthorDaoImpl.class, GenreDaoImpl.class, BookDaoImpl.class, IOServiceStreams.class})
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
        authorService.add("Surname", "NameY", "Patronymic");
        bookService.addAuthor(2, 1);
        bookService.removeAuthor(2, 1);
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
    void setComment() {
        bookService.add("Name1");
        bookService.setComment(1, "comment");
    }
}