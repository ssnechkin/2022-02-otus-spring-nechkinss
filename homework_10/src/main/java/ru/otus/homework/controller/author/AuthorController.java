package ru.otus.homework.controller.author;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.controller.MenuItems;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.dto.in.AuthorDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.*;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.dto.out.content.table.Table;
import ru.otus.homework.dto.out.enums.FieldType;
import ru.otus.homework.dto.out.enums.NotificationType;
import ru.otus.homework.service.author.AuthorService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthorController implements MenuItems {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @Override
    public List<Menu> getMenu() {
        return List.of(new Menu(3, "Авторы", new Link(HttpMethod.GET, "/author/list"), true));
    }

    @GetMapping("/author/list")
    public Content list() {
        Content content = new Content();
        Menu add = new Menu();
        add.setTitle("Добавить");
        add.setLink(new Link(HttpMethod.GET, "/author/add"));
        content.setPageName("Авторы");
        content.setManagement(List.of(add));
        content.setTable(getTableAll());
        return content;
    }

    @GetMapping("/author/view")
    public Content view(@RequestParam("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/author/edit")
    public Content edit(@RequestParam("id") long id) {
        Author author = service.getById(id);
        Content content = new Content();
        Form form = new Form();
        Menu save = new Menu();
        Field surname = new Field(), name = new Field(), patronymic = new Field();
        content.setPageName("Автор-редактирование");
        save.setTitle("Сохранить");
        save.setLink(new Link(HttpMethod.PUT, "/author/save?id=" + author.getId()));
        content.setManagement(List.of(save));
        surname.setType(FieldType.INPUT);
        surname.setLabel("Фамилия");
        surname.setName("surname");
        surname.setValue(author.getSurname());
        name.setType(FieldType.INPUT);
        name.setLabel("Имя");
        name.setName("name");
        name.setValue(author.getName());
        patronymic.setType(FieldType.INPUT);
        patronymic.setLabel("Отчество");
        patronymic.setName("patronymic");
        patronymic.setValue(author.getPatronymic());
        form.setFields(List.of(surname, name, patronymic));
        content.setForm(form);
        return content;
    }

    @PutMapping("/author/save")
    public Content save(@RequestParam("id") long id,
                        @RequestBody AuthorDto authorDto) {
        Author author = service.getById(id);
        Content content = new Content();
        Notification notification = new Notification();
        if (authorDto.getSurname() == null || authorDto.getSurname().isEmpty()
                || authorDto.getName() == null || authorDto.getName().isEmpty()) {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Фамилия и имя должны быть заполнены");
        } else {
            author = service.edit(author, authorDto.getSurname(), authorDto.getName(), authorDto.getPatronymic());
            content = getContentView(id);
            notification.setType(NotificationType.INFO);
            notification.setMessage("Автор сохранен");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    @GetMapping("/author/add")
    public Content add() {
        Content content = new Content();
        Form form = new Form();
        Menu add = new Menu();
        Field surname = new Field(), name = new Field(), patronymic = new Field();
        content.setPageName("Автор - добавление");
        add.setLink(new Link(HttpMethod.POST, "/author/create"));
        add.setTitle("Добавить");
        content.setManagement(List.of(add));
        surname.setType(FieldType.INPUT);
        surname.setLabel("Фамилия");
        surname.setName("surname");
        name.setType(FieldType.INPUT);
        name.setLabel("Имя");
        name.setName("name");
        patronymic.setType(FieldType.INPUT);
        patronymic.setLabel("Отчество");
        patronymic.setName("patronymic");
        form.setFields(List.of(surname, name, patronymic));
        content.setForm(form);
        return content;
    }

    @PostMapping("/author/create")
    public Content create(@RequestBody AuthorDto authorDto) {
        Notification notification = new Notification();
        Content content = new Content();
        if (authorDto.getSurname() == null || authorDto.getSurname().isEmpty()
                || authorDto.getName() == null || authorDto.getName().isEmpty()) {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Фамилия и имя должны быть заполнены");
        } else {
            Author author = service.add(authorDto.getSurname(), authorDto.getName(), authorDto.getPatronymic());
            content = getContentView(author.getId());
            notification.setType(NotificationType.INFO);
            notification.setMessage("Автор добавлен");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    @DeleteMapping("/author/delete")
    public Content delete(@RequestParam("id") long id) {
        Notification notification = new Notification();
        Content content = new Content();
        Author author = service.getById(id);
        Menu add = new Menu();
        boolean result = service.delete(author);
        content.setPageName("Авторы");
        add.setTitle("Добавить");
        add.setLink(new Link(HttpMethod.GET, "/author/add"));
        content.setManagement(List.of(add));
        content.setTable(getTableAll());
        if (result) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Автор удален");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Автор не удален. Автор имеется у книг");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    private Table getTableAll() {
        Table table = new Table();
        table.setLabels(List.of("Фамилия", "Имя", "Отчество"));
        List<Row> rows = new ArrayList<>();
        for (Author author : service.getAll()) {
            Row row = new Row();
            row.setLink(new Link(HttpMethod.GET, "/author/view?id=" + author.getId()));
            row.setColumns(List.of(author.getSurname(), author.getName(), author.getPatronymic() == null ? "" : author.getPatronymic()));
            rows.add(row);
        }
        table.setRows(rows);
        return table;
    }

    private Content getContentView(long id) {
        Author author = service.getById(id);
        Content content = new Content();
        Menu edit = new Menu(), delete = new Menu();
        Field surname = new Field(), name = new Field(), patronymic = new Field();
        content.setPageName("Автор");
        edit.setPosition(1);
        edit.setTitle("Редактировать");
        edit.setLink(new Link(HttpMethod.GET, "/author/edit?id=" + author.getId()));
        delete.setPosition(2);
        delete.setTitle("Удалить");
        delete.setLink(new Link(HttpMethod.DELETE, "/author/delete?id=" + author.getId()));
        content.setManagement(List.of(edit, delete));
        surname.setType(FieldType.INPUT);
        surname.setLabel("Фамилия");
        surname.setName("surname");
        surname.setValue(author.getSurname());
        name.setType(FieldType.INPUT);
        name.setLabel("Имя");
        name.setName("name");
        name.setValue(author.getName());
        patronymic.setType(FieldType.INPUT);
        patronymic.setLabel("Отчество");
        patronymic.setName("patronymic");
        patronymic.setValue(author.getPatronymic());
        content.setFields(List.of(surname, name, patronymic));
        return content;
    }
}