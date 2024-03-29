package ru.otus.homework.controller.author;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.domain.entity.Menu;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.dto.in.AuthorDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.*;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.dto.out.content.table.Table;
import ru.otus.homework.dto.out.enums.FieldType;
import ru.otus.homework.dto.out.enums.NotificationType;
import ru.otus.homework.repository.MenuRepository;
import ru.otus.homework.service.author.AuthorService;
import ru.otus.homework.service.author.AuthorUiService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthorController {

    private final AuthorService service;
    private final AuthorUiService uiService;

    public AuthorController(AuthorService service, AuthorUiService uiService, MenuRepository menuRepository) {
        this.service = service;
        this.uiService = uiService;
        addMenu(menuRepository);
    }

    @GetMapping("/author")
    @HystrixCommand(commandKey="getFallKey", fallbackMethod="fallback")
    public Content list() {
        return new Content().setPageName("Авторы")
                .setManagement(uiService.getManagementFormAdd())
                .setTable(getTableAll());
    }

    @GetMapping("/author/{id}")
    @HystrixCommand(commandKey="getFallKey", fallbackMethod="fallbackId")
    public Content view(@PathVariable("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/author/edit/{id}")
    @HystrixCommand(commandKey="getFallKey", fallbackMethod="fallbackId")
    public Content edit(@PathVariable("id") long id) {
        Author author = service.getById(id);
        return new Content()
                .setPageName("Автор-редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT)
                                        .setValue("/author/" + author.getId())
                                )
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Фамилия")
                                .setName("surname")
                                .setValue(author.getSurname()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Имя")
                                .setName("name")
                                .setValue(author.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Отчество")
                                .setName("patronymic")
                                .setValue(author.getPatronymic())
                )));
    }

    @PutMapping("/author/{id}")
    @HystrixCommand(commandKey="getFallKey", fallbackMethod="fallbackIdAuthorDto")
    public Content save(@PathVariable("id") long id,
                        @RequestBody AuthorDto authorDto) {
        if (authorDto.getSurname() == null || authorDto.getSurname().isEmpty()
                || authorDto.getName() == null || authorDto.getName().isEmpty()) {
            return new Content().setNotifications(List.of(new Notification()
                    .setType(NotificationType.WARNING)
                    .setMessage("Фамилия и имя должны быть заполнены")
            ));
        } else {
            service.edit(service.getById(id), authorDto.getSurname(), authorDto.getName(), authorDto.getPatronymic());
            return getContentView(id).setNotifications(List.of(new Notification()
                    .setType(NotificationType.INFO)
                    .setMessage("Автор сохранен")
            ));
        }
    }

    @GetMapping("/author/add")
    @HystrixCommand(commandKey="getFallKey", fallbackMethod="fallback")
    public Content add() {
        return new Content()
                .setPageName("Автор - добавление")
                .setManagement(List.of(
                        new Button().setTitle("Добавить")
                                .setLink(new Link().setMethod(HttpMethod.POST)
                                        .setValue("/author")
                                )
                ))
                .setForm(new Form().setFields(List.of(
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Фамилия")
                                        .setName("surname"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Имя")
                                        .setName("name"),
                                new Field().setType(FieldType.INPUT)
                                        .setLabel("Отчество")
                                        .setName("patronymic")
                        ))
                );
    }

    @PostMapping("/author")
    @HystrixCommand(commandKey="getFallKey", fallbackMethod="fallbackAuthorDto")
    public Content create(@RequestBody AuthorDto authorDto) {
        if (authorDto.getSurname() == null || authorDto.getSurname().isEmpty()
                || authorDto.getName() == null || authorDto.getName().isEmpty()) {
            return new Content()
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.WARNING)
                                    .setMessage("Фамилия и имя должны быть заполнены")
                    ));
        } else {
            Author author = service.add(authorDto.getSurname(), authorDto.getName(), authorDto.getPatronymic());
            return getContentView(author.getId())
                    .setNotifications(List.of(
                            new Notification().setType(NotificationType.INFO)
                                    .setMessage("Автор добавлен")
                    ));
        }
    }

    @DeleteMapping("/author/{id}")
    @HystrixCommand(commandKey="getFallKey", fallbackMethod="fallbackId")
    public Content delete(@PathVariable("id") long id) {
        Notification notification = new Notification();
        if (service.delete(service.getById(id))) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Автор удален");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Автор не удален. У автора имеется книга");
        }
        return new Content()
                .setPageName("Авторы")
                .setManagement(List.of(new Button().setTitle("Добавить")
                        .setLink(new Link().setMethod(HttpMethod.GET)
                                .setValue("/author/add")
                        )
                ))
                .setTable(getTableAll())
                .setNotifications(List.of(notification));
    }

    private Content fallback() {
        return new Content().setNotifications(List.of(new Notification()
                .setType(NotificationType.ERROR)
                .setMessage("Серер перегружен.")
        ));
    }

    private Content fallbackId(long id) {
        return fallback();
    }

    private Content fallbackIdAuthorDto(long id, AuthorDto authorDto) {
        return fallback();
    }

    private Content fallbackAuthorDto(AuthorDto authorDto) {
        return fallback();
    }

    private void addMenu(MenuRepository menuRepository) {
        if (menuRepository.findByLink("/author").size() == 0) {
            Menu menu = new Menu().setTitle("Авторы")
                    .setPosition(1)
                    .setMethod(HttpMethod.GET.name())
                    .setLink("/author")
                    .setAlt(true);
            menuRepository.save(menu);
        }
    }

    private Table getTableAll() {
        List<Row> rows = new ArrayList<>();
        for (Author author : service.getAll()) {
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET)
                            .setValue("/author/" + author.getId())
                    )
                    .setColumns(List.of(
                            author.getSurname(),
                            author.getName(),
                            author.getPatronymic() == null ? "" : author.getPatronymic()
                    ))
            );
        }
        return new Table()
                .setLabels(List.of("Фамилия", "Имя", "Отчество"))
                .setRows(rows);
    }

    private Content getContentView(long id) {
        Author author = service.getById(id);
        return new Content()
                .setPageName("Автор")
                .setManagement(List.of(
                        new Button().setTitle("Редактировать")
                                .setPosition(1)
                                .setLink(new Link().setMethod(HttpMethod.GET)
                                        .setValue("/author/edit/" + author.getId())
                                )
                        , new Button().setTitle("Удалить")
                                .setPosition(2)
                                .setLink(new Link().setMethod(HttpMethod.DELETE)
                                        .setValue("/author/" + author.getId())
                                )
                ))
                .setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Фамилия")
                                .setName("surname")
                                .setValue(author.getSurname()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Имя")
                                .setName("name")
                                .setValue(author.getName()),
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Отчество")
                                .setName("patronymic")
                                .setValue(author.getPatronymic())
                ));
    }
}