package ru.otus.homework.security.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import ru.otus.homework.ContentGetter;
import ru.otus.homework.controller.book.BookGenresController;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.dto.in.book.BookGenreDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.service.book.BookServiceImpl;
import ru.otus.homework.service.genre.GenreServiceImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Security BookGenresController")
@ComponentScan("ru.otus.homework")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookGenresControllerTest {
    @Autowired
    private BookGenresController bookGenresController;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private GenreServiceImpl genreService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ContentGetter contentGetter;

    @BeforeEach
    public void before() {
        this.contentGetter = new ContentGetter(port, restTemplate, false);
    }

    @Test
    @DisplayName("Список связанных")
    void list() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Genre genre1 = genreService.add("name1" + id);
        Genre genre2 = genreService.add("name2" + id);
        bookService.addGenre(book, genre1);
        bookService.addGenre(book, genre2);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/genres");
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        genreService.delete(genre1);
        genreService.delete(genre2);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Genre genre = genreService.add("name1" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/genres/" + genre.getId());
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/genres/add");
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Добавить запись")
    void create() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Genre genre = genreService.add("name1" + id);
        BookGenreDto bookGenreDto = new BookGenreDto();
        bookGenreDto.setGenre(genre.getId());
        Content content = contentGetter.getContent(HttpMethod.POST, "/book/" + book.getId() + "/genres", bookGenreDto);
        assertNull(content);
        bookService.delete(book);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Genre genre = genreService.add("name1" + id);
        bookService.addGenre(book, genre);
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/book/" + book.getId() + "/genres/" + genre.getId(), null);
        assertNull(content);
        bookService.delete(book);
        genreService.delete(genre);
    }
}