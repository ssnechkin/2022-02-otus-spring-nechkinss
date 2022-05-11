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
import ru.otus.homework.domain.entity.book.BookComment;
import ru.otus.homework.dto.in.book.BookCommentDto;
import ru.otus.homework.dto.in.book.BookDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.Menu;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.service.book.BookServiceImpl;
import ru.otus.homework.service.genre.GenreServiceImpl;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookCommentsController")
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

    @Test
    @DisplayName("Список связанных")
    void list() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookComment bookComment = bookService.addComment(book, "name1" + id);
        BookComment bookComment2 = bookService.addComment(book, "name2" + id);
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/book/comments/list?book_id=" + book.getId(), Content.class);
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
        bookService.delete(book);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookComment bookComment = bookService.addComment(book, "name1" + id);
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/book/comments/view?book_id=" + book.getId() + "&id=" + bookComment.getId(), Content.class);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name1" + id)) {
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
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/book/comments/add?book_id=" + book.getId(), Content.class);
        assertNotNull(content.getForm());
        assertTrue(content.getForm().getFields().size() > 0);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Добавить запись")
    void create() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookCommentDto bookCommentDto = new BookCommentDto();
        bookCommentDto.setComment("name1" + id);
        RequestEntity<BookCommentDto> requestEntity = new RequestEntity<BookCommentDto>(bookCommentDto, HttpMethod.POST, URI.create("http://localhost:" + port + "/book/comments/create?book_id=" + book.getId()));
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange(requestEntity, Content.class);
        Content content = responseEntity.getBody();
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
        BookComment bc = null;
        for (Menu menu : content.getManagement()) {
            if (menu.getLink().getValue().contains("/book/comments/delete?book_id=" + book.getId() + "&id=")) {
                b = bookService.getById(Long.parseLong(menu.getLink().getValue().split("book_id=")[1].split("&")[0]));
                bc = bookService.getBookCommentById(Long.parseLong(menu.getLink().getValue().split("&id=")[1]));
            }
        }
        assertNotNull(b);
        assertNotNull(bc);
        bookService.delete(b);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookComment bookComment = bookService.addComment(book, "name1" + id);
        RequestEntity<BookDto> requestEntity = new RequestEntity<BookDto>(new BookDto(), HttpMethod.DELETE, URI.create("http://localhost:" + port + "/book/comments/delete?book_id=" + book.getId() + "&id=" + bookComment.getId()));
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange(requestEntity, Content.class);
        Content content = responseEntity.getBody();
        assert content != null;
        book = bookService.getById(book.getId());
        BookComment bookComment2 = bookService.getBookCommentById(bookComment.getId());
        assertNull(bookComment2);
        bookService.delete(book);
    }
}