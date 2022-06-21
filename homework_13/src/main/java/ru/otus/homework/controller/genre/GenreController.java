package ru.otus.homework.controller.genre;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.controller.MenuItems;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.dto.in.GenreDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.*;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.dto.out.content.table.Table;
import ru.otus.homework.dto.out.enums.FieldType;
import ru.otus.homework.dto.out.enums.NotificationType;
import ru.otus.homework.service.genre.GenreService;
import ru.otus.homework.service.genre.GenreUiService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GenreController implements MenuItems {

    private final GenreService service;
    private final GenreUiService uiService;

    public GenreController(GenreService service, GenreUiService uiService) {
        this.service = service;
        this.uiService = uiService;
    }

    @Override
    public List<Button> getMenu() {
        return uiService.getMenu();
    }

    @GetMapping("/genre")
    public Content list() {
        return new Content().setPageName("Жанры")
                .setManagement(uiService.getManagementFormAdd())
                .setTable(getTableAll());
    }

    @GetMapping("/genre/{id}")
    public Content view(@PathVariable("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/genre/edit/{id}")
    public Content edit(@PathVariable("id") long id) {
        Genre genre = service.getById(id);
        Content content = new Content();
        Form form = new Form();
        Button save = new Button();
        Field name = new Field(), description = new Field();
        content.setPageName("Жанр-редактирование");
        save.setTitle("Сохранить");
        save.setLink(new Link(HttpMethod.PUT, "/genre/" + genre.getId()));
        content.setManagement(List.of(save));
        name.setType(FieldType.INPUT);
        name.setLabel("Наименование");
        name.setName("name");
        name.setValue(genre.getName());
        description.setType(FieldType.INPUT);
        description.setLabel("Описание");
        description.setName("description");
        description.setValue(genre.getDescription());
        form.setFields(List.of(name, description));
        content.setForm(form);
        return content;
    }

    @PutMapping("/genre/{id}")
    public Content save(@PathVariable("id") long id,
                        @RequestBody GenreDto genreDto) {
        Genre genre = service.getById(id);
        Content content = new Content();
        Notification notification = new Notification();
        if (genreDto.getName() == null || genreDto.getName().isEmpty()) {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Наименование должно быть заполнено");
        } else {
            genre = service.editName(genre, genreDto.getName());
            genre = service.editDescription(genre, genreDto.getDescription());
            content = getContentView(id);
            notification.setType(NotificationType.INFO);
            notification.setMessage("Жанр сохранен");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    @GetMapping("/genre/add")
    public Content add() {
        Content content = new Content();
        Form form = new Form();
        Button add = new Button();
        Field name = new Field(), description = new Field();
        content.setPageName("Жанр - добавление");
        add.setLink(new Link(HttpMethod.POST, "/genre"));
        add.setTitle("Добавить");
        content.setManagement(List.of(add));
        name.setType(FieldType.INPUT);
        name.setLabel("Наименование");
        name.setName("name");
        description.setType(FieldType.INPUT);
        description.setLabel("Описание");
        description.setName("description");
        form.setFields(List.of(name, description));
        content.setForm(form);
        return content;
    }

    @PostMapping("/genre")
    public Content create(@RequestBody GenreDto genreDto) {
        Notification notification = new Notification();
        Content content = new Content();
        if (genreDto.getName() == null || genreDto.getName().isEmpty()) {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Наименование должно быть заполнено");
        } else {
            Genre genre = service.add(genreDto.getName());
            genre = service.editDescription(genre, genreDto.getDescription());
            content = getContentView(genre.getId());
            notification.setType(NotificationType.INFO);
            notification.setMessage("Жанр добавлен");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    @DeleteMapping("/genre/{id}")
    public Content delete(@PathVariable("id") long id) {
        Notification notification = new Notification();
        Content content = new Content();
        Genre genre = service.getById(id);
        Button add = new Button();
        boolean result = service.delete(genre);
        content.setPageName("Жанры");
        add.setTitle("Добавить");
        add.setLink(new Link(HttpMethod.GET, "/genre/add"));
        content.setManagement(List.of(add));
        content.setTable(getTableAll());
        if (result) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Жанр удален");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Жанр не удален. Жанр имеется у книг");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    private Table getTableAll() {
        Table table = new Table();
        table.setLabels(List.of("Наименование", "Описание"));
        List<Row> rows = new ArrayList<>();
        for (Genre genre : service.getAll()) {
            Row row = new Row();
            row.setLink(new Link(HttpMethod.GET, "/genre/" + genre.getId()));
            row.setColumns(List.of(genre.getName(), genre.getDescription() == null ? "" : genre.getDescription()));
            rows.add(row);
        }
        table.setRows(rows);
        return table;
    }

    private Content getContentView(long id) {
        Genre genre = service.getById(id);
        Content content = new Content();
        Button edit = new Button(), delete = new Button();
        Field name = new Field(), description = new Field();
        content.setPageName("Жанр");
        edit.setPosition(1);
        edit.setTitle("Редактировать");
        edit.setLink(new Link(HttpMethod.GET, "/genre/edit/" + genre.getId()));
        delete.setPosition(2);
        delete.setTitle("Удалить");
        delete.setLink(new Link(HttpMethod.DELETE, "/genre/" + genre.getId()));
        content.setManagement(List.of(edit, delete));
        name.setType(FieldType.INPUT);
        name.setLabel("Наименование");
        name.setName("name");
        name.setValue(genre.getName());
        description.setType(FieldType.INPUT);
        description.setLabel("Описание");
        description.setName("description");
        description.setValue(genre.getDescription());
        content.setFields(List.of(name, description));
        return content;
    }
}