package ru.otus.homework.controller.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.dto.in.book.BookDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.Menu;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.service.book.BookServiceImpl;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookController")
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

    @Test
    @DisplayName("Наличие меню")
    void getMenu() {
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/", HttpMethod.POST, null, Content.class);
        Content content = responseEntity.getBody();
        assert content != null;
        List<Menu> menus = bookController.getMenu();
        assertTrue(menus.size() > 0);
        for (Menu menu : bookController.getMenu()) {
            assertTrue(content.getMenu().contains(menu));
            assertNotNull(menu.getLink());
            assertNotNull(menu.getTitle());
            assertNotNull(menu.getAlt());
        }
    }

    @Test
    @DisplayName("Список")
    void list() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/book/list", Content.class);
        String name = null;
        for (Row row : content.getTable().getRows()) {
            for (String column : row.getColumns()) {
                if (column.equals("name" + id)) {
                    name = column;
                    break;
                }
            }
        }
        assertNotNull(name);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/book/view?id=" + book.getId(), Content.class);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Получить форму для редактирования")
    void edit() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/book/edit?id=" + book.getId(), Content.class);
        assertNotNull(content.getForm());
        assertTrue(content.getForm().getFields().size() > 0);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Сохранить изменения записи")
    void save() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookDto bookDto = new BookDto();
        bookDto.setName("1" + book.getName());
        RequestEntity<BookDto> requestEntity = new RequestEntity<BookDto>(bookDto, HttpMethod.PUT, URI.create("http://localhost:" + port + "/book/save?id=" + book.getId()));
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange(requestEntity, Content.class);
        Content content = responseEntity.getBody();
        assert content != null;
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("1name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/book/add", Content.class);
        assertNotNull(content.getForm());
        assertTrue(content.getForm().getFields().size() > 0);
    }

    @Test
    @DisplayName("Добавить запись")
    void create() {
        String id = UUID.randomUUID().toString();
        BookDto bookDto = new BookDto();
        bookDto.setName("1name" + id);
        RequestEntity<BookDto> requestEntity = new RequestEntity<BookDto>(bookDto, HttpMethod.POST, URI.create("http://localhost:" + port + "/book/create"));
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange(requestEntity, Content.class);
        Content content = responseEntity.getBody();
        assert content != null;
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("1name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        Book book = null;
        for (Menu menu : content.getManagement()) {
            if (menu.getLink().getValue().contains("/book/edit?id=")) {
                book = bookService.getById(Long.parseLong(menu.getLink().getValue().split("id=")[1]));
            }
        }
        assertNotNull(book);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        long dId = book.getId();
        RequestEntity<BookDto> requestEntity = new RequestEntity<BookDto>(new BookDto(), HttpMethod.DELETE, URI.create("http://localhost:" + port + "/book/delete?id=" + dId));
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange(requestEntity, Content.class);
        Content content = responseEntity.getBody();
        assert content != null;
        assertNull(bookService.getById(dId));
    }
}