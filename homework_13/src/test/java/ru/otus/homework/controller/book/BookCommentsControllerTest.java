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

    private ContentGetter contentGetter;

    @BeforeEach
    public void before() {
        this.contentGetter = new ContentGetter(port, restTemplate);
    }

    @Test
    @DisplayName("Список связанных")
    void list() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookComment bookComment = bookService.addComment(book, "name1" + id);
        BookComment bookComment2 = bookService.addComment(book, "name2" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/comments");
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
        Content content = contentGetter.getContent("/book/" + book.getId() + "/comments/" + bookComment.getId());
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
        Content content = contentGetter.getContent("/book/" + book.getId() + "/comments/add");
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
        Content content = contentGetter.getContent(HttpMethod.POST, "/book/" + book.getId() + "/comments", bookCommentDto);
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
        for (Button button : content.getManagement()) {
            if (button.getLink().getValue().contains("/book/" + book.getId() + "/comments/")) {
                b = bookService.getById(Long.parseLong(button.getLink().getValue().split("/book/")[1].split("/")[0]));
                bc = bookService.getBookCommentById(Long.parseLong(button.getLink().getValue().split("/book/" + book.getId() + "/comments/")[1]));
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
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/book/" + book.getId() + "/comments/" + bookComment.getId(), null);
        assert content != null;
        book = bookService.getById(book.getId());
        BookComment bookComment2 = bookService.getBookCommentById(bookComment.getId());
        assertNull(bookComment2);
        bookService.delete(book);
    }
}