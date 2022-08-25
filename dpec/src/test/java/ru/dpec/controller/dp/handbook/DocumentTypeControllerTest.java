package ru.dpec.controller.dp.handbook;

import org.junit.jupiter.api.AfterEach;
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
import ru.dpec.domain.dto.in.dp.handbook.DocumentTypeDto;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Button;
import ru.dpec.domain.dto.out.content.Field;
import ru.dpec.domain.dto.out.content.table.Row;
import ru.dpec.domain.entity.dp.handbook.DocumentType;
import ru.dpec.domain.entity.dp.handbook.Scope;
import ru.dpec.service.dp.handbook.DocumentTypeServiceImpl;
import ru.dpec.service.dp.handbook.ScopeServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Класс DocumentTypeController")
@ComponentScan("ru.dpec")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DocumentTypeControllerTest {
    @Autowired
    private DocumentTypeController documentTypeController;

    @Autowired
    private DocumentTypeServiceImpl documentTypeService;

    @Autowired
    private ScopeServiceImpl scopeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ContentGetter contentGetter;
    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "password";
    private Scope scope;

    @BeforeEach
    public void before() {
        this.contentGetter = new ContentGetter(port, restTemplate);
        scope = scopeService.add("name", "mnemonic");
    }

    @AfterEach
    public void after() {
        scopeService.delete(scope);
    }

    @Test
    @DisplayName("Список")
    void list() {
        String id = UUID.randomUUID().toString();
        DocumentType documentType = documentTypeService.add("mnemonic" + id, "name" + id, scopeService.getAll().stream().findFirst().get(), "source" + id);
        Content content = contentGetter.getContent("/handbook/document_type", ADMIN_LOGIN, ADMIN_PASSWORD);
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
        documentTypeService.delete(documentType);
    }

    @Test
    @DisplayName("Просмотр записи")
    void view() {
        String id = UUID.randomUUID().toString();
        DocumentType documentType = documentTypeService.add("mnemonic" + id, "name" + id, scopeService.getAll().stream().findFirst().get(), "source" + id);
        Content content = contentGetter.getContent("/handbook/document_type/" + documentType.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        documentTypeService.delete(documentType);
    }

    @Test
    @DisplayName("Форма редактирования записи")
    void edit() {
        String id = UUID.randomUUID().toString();
        DocumentType documentType = documentTypeService.add("mnemonic" + id, "name" + id, scopeService.getAll().stream().findFirst().get(), "source" + id);
        Content content = contentGetter.getContent("/handbook/document_type/edit/" + documentType.getId(), ADMIN_LOGIN, ADMIN_PASSWORD);
        String name = null;
        for (Field field : content.getForm().getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        documentTypeService.delete(documentType);
    }

    @Test
    @DisplayName("Сохранить изменения записи")
    void save() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Scope scope = scopeService.add("scopename" + id2, "description" + id2);
        DocumentType documentType = documentTypeService.add("mnemonic" + id, "name" + id, scopeService.getAll().stream().findFirst().get(), "source" + id);
        DocumentTypeDto documentTypeUpdate = new DocumentTypeDto();
        documentTypeUpdate.setName("name" + id2);
        documentTypeUpdate.setMnemonic("mnemonic" + id2);
        documentTypeUpdate.setScope(scope.getId());
        Content content = contentGetter.getContent(HttpMethod.PUT, "/handbook/document_type/" + documentType.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, documentTypeUpdate);
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id2)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        documentTypeService.delete(documentType);
    }

    @Test
    @DisplayName("Форма добавления записи")
    void add() {
        Content content = contentGetter.getContent("/handbook/document_type/add", ADMIN_LOGIN, ADMIN_PASSWORD);
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
        DocumentTypeDto documentTypeUpdate = new DocumentTypeDto();
        documentTypeUpdate.setName("name" + id);
        documentTypeUpdate.setMnemonic("mnemonic" + id);
        documentTypeUpdate.setSource("source" + id);
        documentTypeUpdate.setScope(scopeService.getAll().stream().findFirst().get().getId());
        Content content = contentGetter.getContent(HttpMethod.POST, "/handbook/document_type", ADMIN_LOGIN, ADMIN_PASSWORD, documentTypeUpdate);
        System.out.println(content);
        if (content.getFields() == null) {
            Optional<DocumentType> a = documentTypeService.getAll().stream().filter(action -> action.getName().equals("name" + id)).findFirst();
            a.ifPresent(actions -> documentTypeService.delete(a.get()));
        }
        String name = null;
        for (Field field : content.getFields()) {
            if (field.getValue().equals("name" + id)) {
                name = field.getValue();
                break;
            }
        }
        assertNotNull(name);
        Optional<DocumentType> a = documentTypeService.getAll().stream().filter(action -> action.getName().equals("name" + id)).findFirst();
        a.ifPresent(actions -> documentTypeService.delete(a.get()));
    }

    @Test
    @DisplayName("Удаление записи")
    void delete() {
        String id = UUID.randomUUID().toString();
        DocumentType documentType = documentTypeService.add("mnemonic" + id, "name" + id, scopeService.getAll().stream().findFirst().get(), "source" + id);
        contentGetter.getContent(HttpMethod.DELETE, "/handbook/document_type/" + documentType.getId(), ADMIN_LOGIN, ADMIN_PASSWORD, null);
        Optional<DocumentType> a = documentTypeService.getAll().stream().filter(act -> act.getName().equals("name" + id)).findFirst();
        assertFalse(a.isPresent());
        a.ifPresent(actions -> documentTypeService.delete(actions));
    }
}