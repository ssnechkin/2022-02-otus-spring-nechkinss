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
import ru.otus.homework.controller.book.BookCommentsController;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.book.BookComment;
import ru.otus.homework.dto.in.book.BookCommentDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.service.book.BookServiceImpl;
import ru.otus.homework.service.genre.GenreServiceImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Security BookCommentsController")
@ComponentScan("ru.otus.homework")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookCommentsControllerTest {

    @Autowired
    private BookCommentsController bookController;

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
        this.contentGetter = new ContentGetter(port, restTemplate, false);
    }

    @Test
    @DisplayName("Список связанных")
    void list() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookComment bookComment = bookService.addComment(book, "name1" + id);
        BookComment bookComment2 = bookService.addComment(book, "name2" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/comments", ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookComment bookComment = bookService.addComment(book, "name1" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/comments/" + bookComment.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/comments/add", ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Добавить запись")
    void create() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookCommentDto bookCommentDto = new BookCommentDto();
        bookCommentDto.setComment("name1" + id);
        Content content = contentGetter.getContent(HttpMethod.POST, "/book/" + book.getId() + "/comments", ADMIN_LOGIN, ADMIN_PASSWORD, bookCommentDto);
        assertNull(content);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookComment bookComment = bookService.addComment(book, "name1" + id);
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/book/" + book.getId() + "/comments/" + bookComment.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, null);
        assertNull(content);
        bookService.delete(book);
    }
}