package ru.otus.homework.controller.author;

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
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.dto.in.AuthorDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.Menu;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.service.author.AuthorServiceImpl;

import java.net.URI;
import java.util.List;
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

    @Test
    @DisplayName("Наличие меню")
    void getMenu() {
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/", HttpMethod.POST, null, Content.class);
        Content content = responseEntity.getBody();
        assert content != null;
        List<Menu> menus = authorController.getMenu();
        assertTrue(menus.size() > 0);
        for (Menu menu : authorController.getMenu()) {
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
        Author author = authorService.add("surname" + id, "name" + id, "patronymic" + id);
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/author/list", Content.class);
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
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/author/view?id=" + author.getId(), Content.class);
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
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/author/edit?id=" + author.getId(), Content.class);
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
        RequestEntity<AuthorDto> requestEntity = new RequestEntity<AuthorDto>(authorDto, HttpMethod.PUT, URI.create("http://localhost:" + port + "/author/save?id=" + author.getId()));
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
        authorService.delete(author);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        Content content = this.restTemplate.getForObject("http://localhost:" + port + "/author/add", Content.class);
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
        RequestEntity<AuthorDto> requestEntity = new RequestEntity<AuthorDto>(authorDto, HttpMethod.POST, URI.create("http://localhost:" + port + "/author/create"));
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
        Author author = null;
        for (Menu menu : content.getManagement()) {
            if (menu.getLink().getValue().contains("/author/edit?id=")) {
                author = authorService.getById(Long.parseLong(menu.getLink().getValue().split("id=")[1]));
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
        RequestEntity<AuthorDto> requestEntity = new RequestEntity<AuthorDto>(new AuthorDto(), HttpMethod.DELETE, URI.create("http://localhost:" + port + "/author/delete?id=" + dId));
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange(requestEntity, Content.class);
        Content content = responseEntity.getBody();
        assert content != null;
        assertNull(authorService.getById(dId));
    }
}