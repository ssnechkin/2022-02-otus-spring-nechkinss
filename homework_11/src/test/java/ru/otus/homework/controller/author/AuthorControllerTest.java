package ru.otus.homework.controller.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.dto.AuthorDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Класс AuthorController")
@ComponentScan("ru.otus.homework")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Cписок")
    void list() {
        String id = UUID.randomUUID().toString();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setSurname("1s" + id);
        authorDto.setName("1name" + id);
        authorDto.setPatronymic("1p" + id);
        authorDto = this.restTemplate.postForObject("http://localhost:" + port + "/author", authorDto, AuthorDto.class);
        assertEquals(authorDto.getName(), "1name" + id);
        assertNotNull(authorDto.getId());
        String dId = authorDto.getId();
        AuthorDto[] authors = this.restTemplate.getForObject("http://localhost:" + port + "/author", AuthorDto[].class);
        String name = null;
        for (AuthorDto author : authors) {
            if (author.getName().equals("1name" + id)) {
                name = author.getName();
                System.out.println("---------------");
                break;
            }
        }
        assertNotNull(name);
        this.restTemplate.delete("http://localhost:" + port + "/author/" + dId);
    }

    @Test
    @DisplayName("Просмотр")
    void view() {
        String id = UUID.randomUUID().toString();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setSurname("1s" + id);
        authorDto.setName("1name" + id);
        authorDto.setPatronymic("1p" + id);
        authorDto = this.restTemplate.postForObject("http://localhost:" + port + "/author", authorDto, AuthorDto.class);
        assertEquals(authorDto.getName(), "1name" + id);
        assertNotNull(authorDto.getId());
        String iDd = authorDto.getId();
        AuthorDto author = this.restTemplate.getForObject("http://localhost:" + port + "/author/" + iDd, AuthorDto.class);
        assertNotNull(author.getId());
        assertEquals(author.getName(), "1name" + id);
        assertEquals(author.getSurname(), "1s" + id);
        assertEquals(author.getPatronymic(), "1p" + id);
        this.restTemplate.delete("http://localhost:" + port + "/author/" + iDd);
    }

    @Test
    @DisplayName("Добавление")
    void add() {
        String id = UUID.randomUUID().toString();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setSurname("1s" + id);
        authorDto.setName("1name" + id);
        authorDto.setPatronymic("1p" + id);
        authorDto = this.restTemplate.postForObject("http://localhost:" + port + "/author", authorDto, AuthorDto.class);
        assertEquals(authorDto.getName(), "1name" + id);
        assertNotNull(authorDto.getId());
        this.restTemplate.delete("http://localhost:" + port + "/author/" + authorDto.getId());
    }

    @Test
    @DisplayName("Изменение")
    void save() {
        String id = UUID.randomUUID().toString();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setSurname("1s" + id);
        authorDto.setName("1name" + id);
        authorDto.setPatronymic("1p" + id);
        authorDto = this.restTemplate.postForObject("http://localhost:" + port + "/author", authorDto, AuthorDto.class);
        assertEquals(authorDto.getName(), "1name" + id);
        assertNotNull(authorDto.getId());
        String iDd = authorDto.getId();

        AuthorDto authorDtoUpdate = new AuthorDto();
        authorDtoUpdate.setSurname("2s" + id);
        authorDtoUpdate.setName("2name" + id);
        authorDtoUpdate.setPatronymic("2p" + id);
        this.restTemplate.put("http://localhost:" + port + "/author/" + iDd, authorDtoUpdate);

        AuthorDto author = this.restTemplate.getForObject("http://localhost:" + port + "/author/" + iDd, AuthorDto.class);
        assertNotNull(author.getId());
        assertEquals(author.getName(), "2name" + id);
        assertEquals(author.getSurname(), "2s" + id);
        assertEquals(author.getPatronymic(), "2p" + id);
        this.restTemplate.delete("http://localhost:" + port + "/author/" + iDd);
    }

    @Test
    @DisplayName("Удаление")
    void delete() {
        String id = UUID.randomUUID().toString();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setSurname("1s" + id);
        authorDto.setName("1name" + id);
        authorDto.setPatronymic("1p" + id);
        authorDto = this.restTemplate.postForObject("http://localhost:" + port + "/author", authorDto, AuthorDto.class);
        assertEquals(authorDto.getName(), "1name" + id);
        assertNotNull(authorDto.getId());
        String iDd = authorDto.getId();
        this.restTemplate.delete("http://localhost:" + port + "/author/" + iDd);
        AuthorDto author = this.restTemplate.getForObject("http://localhost:" + port + "/author/" + iDd, AuthorDto.class);
        assertNull(author);
    }
}