package ru.otus.homework.controller.genre;

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

@DisplayName("Класс GenreController")
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

    @BeforeEach
    public void before() {
        this.contentGetter = new ContentGetter(port, restTemplate);
    }

    @Test
    @DisplayName("Список")
    void list() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        Content content = contentGetter.getContent("/genre");
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
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        Content content = contentGetter.getContent("/genre/" + genre.getId());
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Получить форму для редактирования")
    void edit() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        Content content = contentGetter.getContent("/genre/edit/" + genre.getId());
        assertNotNull(content.getForm());
        assertTrue(content.getForm().getFields().size() > 0);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Сохранить изменения записи")
    void save() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        GenreDto genreDto = new GenreDto();
        genreDto.setName("1" + genre.getName());
        Content content = contentGetter.getContent(HttpMethod.PUT, "/genre/" + genre.getId(), genreDto);
        assert content != null;
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("1name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Получить форму для добавления")
    void add() {
        Content content = contentGetter.getContent("/genre/add");
        assertNotNull(content.getForm());
        assertTrue(content.getForm().getFields().size() > 0);
    }

    @Test
    @DisplayName("Добавить запись")
    void create() {
        String id = UUID.randomUUID().toString();
        GenreDto genreDto = new GenreDto();
        genreDto.setName("1name" + id);
        Content content = contentGetter.getContent(HttpMethod.POST, "/genre", genreDto);
        assert content != null;
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("1name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        Genre genre = null;
        for (Button button : content.getManagement()) {
            if (button.getLink().getValue().contains("/genre/edit/")) {
                genre = genreService.getById(Long.parseLong(button.getLink().getValue().split("/genre/edit/")[1]));
            }
        }
        assertNotNull(genre);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Удалить запись")
    void delete() {
        String id = UUID.randomUUID().toString();
        Genre genre = genreService.add("name" + id);
        long dId = genre.getId();
        Content content = contentGetter.getContent(HttpMethod.DELETE, "/genre/" + dId, null);
        assert content != null;
        assertNull(genreService.getById(dId));
    }
}