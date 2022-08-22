package ru.dpec.controller.dp.handbook;

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
import ru.dpec.domain.dto.in.dp.handbook.PurposesDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Button;
import ru.dpec.domain.dto.out.content.Field;
import ru.dpec.domain.dto.out.content.table.Row;
import ru.dpec.domain.entity.dp.handbook.Purposes;
import ru.dpec.service.dp.handbook.PurposesServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Класс PurposesController")
@ComponentScan("ru.dpec")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PurposesControllerTest {
    @Autowired
    private PurposesController purposesController;

    @Autowired
    private PurposesServiceImpl purposesService;

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
        Purposes purposes = purposesService.add("mnemonic" + id, "name" + id);
        Content content = contentGetter.getContent("/handbook/purposes", ADMIN_LOGIN, ADMIN_PASSWORD);
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
        purposesService.delete(purposes);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        Purposes purposes = purposesService.add("mnemonic" + id, "name" + id);
        Content content = contentGetter.getContent("/handbook/purposes/" + purposes.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        purposesService.delete(purposes);
    }

    @Test
    @DisplayName("Форма редактирования записи")
    void edit() {
        String id = UUID.randomUUID().toString();
        Purposes purposes = purposesService.add("mnemonic" + id, "name" + id);
        Content content = contentGetter.getContent("/handbook/purposes/edit/" + purposes.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        String name = null;
        for (Field field : content.getForm().getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        purposesService.delete(purposes);
    }

    @Test
    @DisplayName("Сохранить изменения записи")
    void save() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Purposes action = purposesService.add("mnemonic" + id, "name" + id);
        PurposesDto actionUpdate = new PurposesDto();
        actionUpdate.setName("name" + id2);
        actionUpdate.setMnemonic("mnemonic" + id2);
        Content content = contentGetter.getContent(HttpMethod.PUT, "/handbook/purposes/" + action.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, actionUpdate);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id2)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        purposesService.delete(action);
    }

    @Test
    @DisplayName("Форма добавления записи")
    void add() {
        Content content = contentGetter.getContent("/handbook/purposes/add", ADMIN_LOGIN, ADMIN_PASSWORD);
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
        Purposes actionUpdate = new Purposes();
        actionUpdate.setName("name" + id);
        actionUpdate.setMnemonic("mnemonic" + id);
        Content content = contentGetter.getContent(HttpMethod.POST, "/handbook/purposes", ADMIN_LOGIN, ADMIN_PASSWORD, actionUpdate);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        Optional<Purposes> a = purposesService.getAll().stream().filter(action -> action.getName().equals("name" + id)).findFirst();
        a.ifPresent(purposes -> purposesService.delete(purposes));
    }

    @Test
    @DisplayName("Удаление записи")
    void delete() {
        String id = UUID.randomUUID().toString();
        Purposes action = purposesService.add("mnemonic" + id, "name" + id);
        contentGetter.getContent(HttpMethod.DELETE, "/handbook/purposes/" + action.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, null);
        Optional<Purposes> a = purposesService.getAll().stream().filter(act -> act.getName().equals("name" + id)).findFirst();
        assertFalse(a.isPresent());
        a.ifPresent(purposes -> purposesService.delete(purposes));
    }
}