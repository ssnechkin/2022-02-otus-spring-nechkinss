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
import ru.otus.homework.controller.book.BookController;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.dto.in.book.BookDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.service.book.BookServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Security BookController")
@ComponentScan("ru.otus.homework")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    @Autowired
    private BookController bookController;

    @Autowired
    private BookServiceImpl bookService;

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
    @DisplayName("Наличие меню")
    void getButton() {
        Content content = contentGetter.getContent("/menu");
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
    }

    @Test
    @DisplayName("Список")
    void list() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = contentGetter.getContent("/book");
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = contentGetter.getContent("/book/" + book.getId());
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Получить форму для редактирования")
    void edit() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = contentGetter.getContent("/book/edit/" + book.getId());
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Сохранить изменения записи")
    void save() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookDto bookDto = new BookDto();
        bookDto.setName("1" + book.getName());
        Content content = contentGetter.getContent(HttpMethod.PUT, "/book/" + book.getId(), bookDto);
        assertNull(content);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        Content content = contentGetter.getContent("/book/add");
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
    }

    @Test
    @DisplayName("Добавить запись")
    void create() {
        String id = UUID.randomUUID().toString();
        BookDto bookDto = new BookDto();
        bookDto.setName("1name" + id);

        Content content = contentGetter.getContent(HttpMethod.POST, "/book", bookDto);
        assertNull(content);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        long dId = book.getId();
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/book/" + dId, new BookDto());
        assertNull(content);
    }
}