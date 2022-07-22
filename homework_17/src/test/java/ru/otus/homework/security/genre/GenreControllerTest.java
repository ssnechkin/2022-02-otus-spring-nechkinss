package ru.otus.homework.security.genre;

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
import ru.otus.homework.controller.genre.GenreController;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.dto.in.GenreDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Field;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.service.genre.GenreServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Security GenreController")
@ComponentScan("ru.otus.homework")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GenreControllerTest {
    @Autowired
    private GenreController genreController;

    @Autowired
    private GenreServiceImpl genreService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ContentGetter contentGetter;
    private ContentGetter contentGetterAuth;
    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "password";
    private static final String VISITOR_LOGIN = "visitor";
    private static final String VISITOR_PASSWORD = "123";

    @BeforeEach
    public void before() {
        this.contentGetter = new ContentGetter(port, restTemplate, false);
        this.contentGetterAuth = new ContentGetter(port, restTemplate);
    }
    @Test
    @DisplayName("Доступность меню")
    void getMenu() {
        Content content = contentGetter.getContent("/menu", ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
    }

    @Test
    @DisplayName("Ограниченное меню")
    void getLimitedMenu() {
        Content content = contentGetterAuth.getContent("/menu", VISITOR_LOGIN, VISITOR_PASSWORD);
        assertEquals(content.getPageName(), "Добро пожаловать в библиотеку книг");
        boolean isExistMenu = false;
        for (Button button : content.getButtons()) {
            if (button.getLink().getValue().equals("/genre")) isExistMenu = true;
        }
        assertFalse(isExistMenu);
    }

    @Test
    @DisplayName("Список")
    void list() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        Content content = contentGetter.getContent("/genre", ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        Content content = contentGetter.getContent("/genre/" + genre.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Получить форму для редактирования")
    void edit() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        Content content = contentGetter.getContent("/genre/edit/" + genre.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Сохранить изменения записи")
    void save() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        GenreDto genreDto = new GenreDto();
        genreDto.setName("1" + genre.getName());
        Content content = contentGetter.getContent(HttpMethod.PUT, "/genre/" + genre.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, genreDto);
        assertNull(content);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        Content content = contentGetter.getContent("/genre/add", ADMIN_LOGIN, ADMIN_PASSWORD);
        assertEquals(content.getPageName(), "Вход");
        assertEquals(0, content.getButtons().size());
    }

    @Test
    @DisplayName("Добавить запись")
    void create() {
        String id = UUID.randomUUID().toString();
        GenreDto genreDto = new GenreDto();
        genreDto.setName("1name" + id);
        Content content = contentGetter.getContent(HttpMethod.POST, "/genre", ADMIN_LOGIN, ADMIN_PASSWORD, genreDto);
        assertNull(content);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        long dId = genre.getId();
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/genre/" + dId, ADMIN_LOGIN, ADMIN_PASSWORD, null);
        assertNull(content);
    }
}