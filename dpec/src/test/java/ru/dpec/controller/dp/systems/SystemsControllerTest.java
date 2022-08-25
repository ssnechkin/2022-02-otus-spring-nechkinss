package ru.dpec.controller.dp.systems;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import ru.dpec.ContentGetter;
import ru.dpec.domain.dto.in.dp.systems.SystemsDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Button;
import ru.dpec.domain.dto.out.content.Field;
import ru.dpec.domain.dto.out.content.table.Row;
import ru.dpec.domain.entity.dp.Systems;
import ru.dpec.service.dp.systems.SystemsService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Класс SystemsController")
@ComponentScan("ru.dpec")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SystemsControllerTest {

    @Autowired
    private SystemsController systemsController;

    @Autowired
    private SystemsService service;

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
        Systems systems = service.add("name" + id, "description" + id);
        Content content = contentGetter.getContent("/systems", ADMIN_LOGIN, ADMIN_PASSWORD);
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
        service.delete(systems);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Systems systems = service.add("name" + id, "description" + id);
        Content content = contentGetter.getContent("/systems/" + systems.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        service.delete(systems);
    }

    @Test
    @DisplayName("Форма редактирования записи")
    void edit() {
        String id = UUID.randomUUID().toString();
        Systems systems = service.add("name" + id, "description" + id);
        Content content = contentGetter.getContent("/systems/" + systems.getId() + "/edit/", ADMIN_LOGIN, ADMIN_PASSWORD);
        String name = null;
        for (Field field : content.getForm().getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        service.delete(systems);
    }

    @Test
    @DisplayName("Сохранить изменения записи")
    void save() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Systems systems = service.add("name" + id, "description" + id);
        SystemsDto systemsUpdate = new SystemsDto();
        systemsUpdate.setName("name" + id2);
        systemsUpdate.setDescription("description" + id2);
        Content content = contentGetter.getContent(HttpMethod.PUT, "/systems/" + systems.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, systemsUpdate);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id2)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        service.delete(systems);
    }

    @Test
    @DisplayName("Форма добавления записи")
    void add() {
        Content content = contentGetter.getContent("/systems/add", ADMIN_LOGIN, ADMIN_PASSWORD);
        String buttonName = null;
        for (Button button : content.getManagement()) {
            if (button.getTitle().equals("Добавить")) {
                buttonName = button.getTitle();
                break;
            }
        }
        assertNotNull(buttonName);
        String name = null;
        for (Field field : content.getForm().getFields()) {
            if (field.getName().equals("name")) {
                name = field.getName();
                break;
            }
        }
        assertNotNull(name);
    }

    @Test
    @DisplayName("Добавления записи")
    void create() {
        String id = UUID.randomUUID().toString();
        SystemsDto systemsUpdate = new SystemsDto();
        systemsUpdate.setName("name" + id);
        systemsUpdate.setDescription("description" + id);
        Content content = contentGetter.getContent(HttpMethod.POST, "/systems", ADMIN_LOGIN, ADMIN_PASSWORD, systemsUpdate);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        Optional<Systems> a = service.getAll().stream().filter(systems -> systems.getName().equals("name" + id)).findFirst();
        a.ifPresent(systems -> service.delete(systems));
    }

    @Test
    @DisplayName("Удаление записи")
    void delete() {
        String id = UUID.randomUUID().toString();
        Systems systems = service.add("name" + id, "description" + id);
        contentGetter.getContent(HttpMethod.DELETE, "/systems/" + systems.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, null);
        Optional<Systems> a = service.getAll().stream().filter(act -> act.getName().equals("name" + id)).findFirst();
        assertFalse(a.isPresent());
        a.ifPresent(s -> service.delete(s));
    }
}