package ru.otus.homework.controller.author;

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
import ru.otus.homework.dto.in.AuthorDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.service.author.AuthorServiceImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Класс AuthorController")
@ComponentScan("ru.otus.homework")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorControllerTest {
    @Autowired
    private AuthorController authorController;

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
        this.contentGetter = new ContentGetter(port, restTemplate);
    }

    @Test
    @DisplayName("Список")
    void list() {
        String id = UUID.randomUUID().toString();
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        Content content = contentGetter.getContent("/author", ADMIN_LOGIN, ADMIN_PASSWORD);
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
        authorService.delete(author);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        Content content = contentGetter.getContent("/author/" + author.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        authorService.delete(author);
    }

    @Test
    @DisplayName("Получить форму для редактирования")
    void edit() {
        String id = UUID.randomUUID().toString();
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        Content content = contentGetter.getContent("/author/edit/" + author.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        assertNotNull(content.getForm());
        assertTrue(content.getForm().getFields().size() > 0);
        authorService.delete(author);
    }

    @Test
    @DisplayName("Сохранить изменения записи")
    void save() {
        String id = UUID.randomUUID().toString();
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        AuthorDto authorDto = new AuthorDto();
        authorDto.setSurname("1" + author.getSurname());
        authorDto.setName("1" + author.getName());
        authorDto.setPatronymic("1" + author.getPatronymic());
        Content content = contentGetter.getContent(HttpMethod.PUT, "/author/" + author.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, authorDto);
        assert content != null;
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("1name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        authorService.delete(author);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        Content content = contentGetter.getContent("/author/add", ADMIN_LOGIN, ADMIN_PASSWORD);
        assertNotNull(content.getForm());
        assertTrue(content.getForm().getFields().size() > 0);
    }

    @Test
    @DisplayName("Добавить запись")
    void create() {
        String id = UUID.randomUUID().toString();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setSurname("1s" + id);
        authorDto.setName("1name" + id);
        authorDto.setPatronymic("1p" + id);
        Content content = contentGetter.getContent(HttpMethod.POST, "/author", ADMIN_LOGIN, ADMIN_PASSWORD, authorDto);
        assert content != null;
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("1name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        Author author = null;
        for (Button button : content.getManagement()) {
            if (button.getLink().getValue().contains("/author/edit/")) {
                author = authorService.getById(Long.parseLong(button.getLink().getValue().split("/author/edit/")[1]));
            }
        }
        assertNotNull(author);
        authorService.delete(author);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        long dId = author.getId();
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/author/" + dId, ADMIN_LOGIN, ADMIN_PASSWORD, null);
        assert content != null;
        assertNull(authorService.getById(dId));
    }
}