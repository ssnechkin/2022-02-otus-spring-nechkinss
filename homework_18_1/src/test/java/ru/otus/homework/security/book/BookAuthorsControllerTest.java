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
import ru.otus.homework.controller.book.BookAuthorsController;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.dto.in.book.BookAuthorDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.service.author.AuthorServiceImpl;
import ru.otus.homework.service.book.BookServiceImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Security BookAuthorsController")
@ComponentScan("ru.otus.homework")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookAuthorsControllerTest {
    @Autowired
    private BookAuthorsController bookAuthorsController;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private AuthorServiceImpl authorService;

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
        Author author1 = authorService.add("surname" + id, "name1" + id, "patronymic" + id);
        Author author2 = authorService.add("surname" + id, "name2" + id, "patronymic" + id);
        bookService.addAuthor(book, author1);
        bookService.addAuthor(book, author2);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/authors", ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Author author = authorService.add("surname" + id, "name1" + id, "patronymic" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/authors/" + author.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
        authorService.delete(author);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/authors/add", ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Добавить запись")
    void create() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Author author = authorService.add("surname" + id, "name1" + id, "patronymic" + id);
        BookAuthorDto bookAuthorDto = new BookAuthorDto();
        bookAuthorDto.setAuthor(author.getId());
        Content content = contentGetter.getContent(HttpMethod.POST, "/book/" + book.getId() + "/authors", ADMIN_LOGIN, ADMIN_PASSWORD, bookAuthorDto);
        assertNull(content);
        bookService.delete(book);
        authorService.delete(author);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Author author = authorService.add("surname" + id, "name1" + id, "patronymic" + id);
        bookService.addAuthor(book, author);
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/book/" + book.getId() + "/authors/" + author.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, null);
        assertNull(content);
        bookService.delete(book);
        authorService.delete(author);
    }
}