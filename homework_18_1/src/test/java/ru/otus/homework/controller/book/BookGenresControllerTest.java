package ru.otus.homework.controller.book;

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

@DisplayName("Класс BookGenresController")
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
    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "password";

    @BeforeEach
    public void before() {
        this.contentGetter = new ContentGetter(port, restTemplate);
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
        Content content = contentGetter.getContent("/book/" + book.getId() + "/genres",ADMIN_LOGIN,ADMIN_PASSWORD);
        String name1 = null;
        String name2 = null;
        for (Row row : content.getTable().getRows()) {
            for (String column : row.getColumns()) {
                if (column.equals("name1" + id)) {
                    name1 = column;
                }
                if (column.equals("name2" + id)) {
                    name2 = column;
                }
            }
        }
        assertNotNull(name1);
        assertNotNull(name2);
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
        Content content = contentGetter.getContent("/book/" + book.getId() + "/genres/" + genre.getId(),ADMIN_LOGIN,ADMIN_PASSWORD);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name1" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        bookService.delete(book);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/genres/add",ADMIN_LOGIN,ADMIN_PASSWORD);
        assertNotNull(content.getForm());
        assertTrue(content.getForm().getFields().size() > 0);
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
        Content content = contentGetter.getContent(HttpMethod.POST, "/book/" + book.getId() + "/genres",ADMIN_LOGIN,ADMIN_PASSWORD, bookGenreDto);
        assert content != null;
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name1" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        Book b = null;
        Genre g = null;
        for (Button button : content.getManagement()) {
            if (button.getLink().getValue().contains("/book/" + book.getId() + "/genres/")) {
                b = bookService.getById(Long.parseLong(button.getLink().getValue().split("/book/")[1].split("/")[0]));
                g = genreService.getById(Long.parseLong(button.getLink().getValue().split("/book/" + book.getId() + "/genres/")[1]));

            }
        }
        assertNotNull(b);
        assertNotNull(g);
        bookService.delete(b);
        genreService.delete(g);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Genre genre = genreService.add("name1" + id);
        bookService.addGenre(book, genre);
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/book/" + book.getId() + "/genres/" + genre.getId(),ADMIN_LOGIN,ADMIN_PASSWORD, null);
        assert content != null;
        book = bookService.getById(book.getId());
        genre = genreService.getById(genre.getId());
        assertFalse(genre.getBooks().contains(book));
        bookService.delete(book);
        genreService.delete(genre);
    }
}