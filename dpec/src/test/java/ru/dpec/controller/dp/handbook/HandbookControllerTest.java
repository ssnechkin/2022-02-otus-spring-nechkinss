package ru.dpec.controller.dp.handbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import ru.dpec.ContentGetter;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Button;
import ru.dpec.service.ui.handbook.HandbookUiServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Класс HandbookController")
@ComponentScan("ru.dpec")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HandbookControllerTest {
    @Autowired
    private HandbookController handbookController;

    @Autowired
    private HandbookUiServiceImpl handbookUiService;

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
        Content content = contentGetter.getContent("/handbook", ADMIN_LOGIN, ADMIN_PASSWORD);
        assertTrue(content.getManagement().size() >= 3);
    }
}