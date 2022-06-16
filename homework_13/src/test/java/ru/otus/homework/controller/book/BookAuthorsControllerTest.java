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

@DisplayName("Класс BookAuthorsController")
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

    @BeforeEach
    public void before() {
        this.contentGetter = new ContentGetter(port, restTemplate);
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
        Content content = contentGetter.getContent("/book/" + book.getId() + "/authors");
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
        authorService.delete(author1);
        authorService.delete(author2);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Author author = authorService.add("surname" + id, "name1" + id, "patronymic" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/authors/" + author.getId());
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name1" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        bookService.delete(book);
        authorService.delete(author);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Content content = contentGetter.getContent("/book/" + book.getId() + "/authors/add");
        assertNotNull(content.getForm());
        assertTrue(content.getForm().getFields().size() > 0);
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
        Content content = contentGetter.getContent(HttpMethod.POST, "/book/" + book.getId() + "/authors", bookAuthorDto);
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
        Author a = null;
        for (Button button : content.getManagement()) {
            if (button.getLink().getValue().contains("/book/" + book.getId() + "/authors/")) {
                b = bookService.getById(Long.parseLong(button.getLink().getValue().split("/book/")[1].split("/")[0]));
                a = authorService.getById(Long.parseLong(button.getLink().getValue().split("/book/" + book.getId() + "/authors/")[1]));
            }
        }
        assertNotNull(b);
        assertNotNull(a);
        bookService.delete(b);
        authorService.delete(a);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        Author author = authorService.add("surname" + id, "name1" + id, "patronymic" + id);
        bookService.addAuthor(book, author);
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/book/" + book.getId() + "/authors/" + author.getId(), null);
        assert content != null;
        book = bookService.getById(book.getId());
        author = authorService.getById(author.getId());
        assertFalse(author.getBooks().contains(book));
        bookService.delete(book);
        authorService.delete(author);
    }
}