package ru.otus.homework.controller.book;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.dto.in.book.BookGenreDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.*;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.dto.out.content.table.Table;
import ru.otus.homework.dto.out.enums.FieldType;
import ru.otus.homework.dto.out.enums.NotificationType;
import ru.otus.homework.service.book.BookService;
import ru.otus.homework.service.genre.GenreService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookGenresController {

    private final BookService service;
    private final GenreService genreService;

    public BookGenresController(BookService service, GenreService genreService) {
        this.service = service;
        this.genreService = genreService;
    }

    @GetMapping("/book/{book_id}/genres")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public Content list(@PathVariable("book_id") long bookId) {
        Content content = new Content();
        Button add = new Button();
        add.setTitle("Добавить жанр");
        add.setLink(new Link(HttpMethod.GET, "/book/" + bookId + "/genres/add"));
        content.setPageName("Жанры у книги");
        content.setManagement(List.of(add));
        content.setTable(getTableAll(bookId));
        return content;
    }

    @GetMapping("/book/{book_id}/genres/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public Content view(@PathVariable("id") long id,
                        @PathVariable("book_id") long bookId) {
        return getContentView(bookId, id);
    }

    @GetMapping("/book/{book_id}/genres/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public Content add(@PathVariable("book_id") long bookId) {
        Content content = new Content();
        Form form = new Form();
        Button add = new Button();
        Field name = new Field();
        content.setPageName("Жанр у книги - добавление");
        add.setLink(new Link(HttpMethod.POST, "/book/" + bookId + "/genres"));
        add.setTitle("Добавить");
        content.setManagement(List.of(add));
        name.setType(FieldType.SELECT);
        name.setLabel("Жанр");
        name.setName("genre");
        List<ValueItem> valueItems = new ArrayList<>();
        for (Genre genre : genreService.getAll()) {
            valueItems.add(new ValueItem(genre.getId(), genre.getName()));
        }
        name.setValues(valueItems);
        form.setFields(List.of(name));
        content.setForm(form);
        return content;
    }

    @PostMapping("/book/{book_id}/genres")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public Content create(@RequestBody BookGenreDto bookGenreDto,
                          @PathVariable("book_id") long bookId) {
        Notification notification = new Notification();
        Content content;
        Book book = service.getById(bookId);
        Genre genre = genreService.getById(bookGenreDto.getGenre());
        service.addGenre(book, genre);
        content = getContentView(bookId, genre.getId());
        notification.setType(NotificationType.INFO);
        notification.setMessage("Жанр добавлен к книге");
        content.setNotifications(List.of(notification));
        return content;
    }

    @DeleteMapping("/book/{book_id}/genres/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public Content delete(@PathVariable("book_id") long bookId,
                          @PathVariable("id") long id) {
        Notification notification = new Notification();
        Content content = new Content();
        Book book = service.getById(bookId);
        Genre genre = genreService.getById(id);
        boolean result = service.deleteGenre(book, genre);
        Button add = new Button();
        content.setPageName("Жанры у книги");
        add.setTitle("Добавить");
        add.setLink(new Link(HttpMethod.GET, "/book/" + bookId + "/genres/add"));
        content.setManagement(List.of(add));
        content.setTable(getTableAll(bookId));
        if (result) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Жанр удален у книги");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Не удалось удалить. Жанр отсутствует у книги");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    private Table getTableAll(long bookId) {
        Table table = new Table();
        table.setLabels(List.of("Наименование", "Описание"));
        List<Row> rows = new ArrayList<>();
        for (Genre genre : service.getById(bookId).getGenres()) {
            Row row = new Row();
            row.setLink(new Link(HttpMethod.GET, "/book/" + bookId + "/genres/" + genre.getId()));
            row.setColumns(List.of(genre.getName(), genre.getDescription() == null ? "" : genre.getDescription()));
            rows.add(row);
        }
        table.setRows(rows);
        return table;
    }

    private Content getContentView(long bookId, long id) {
        Genre genre = genreService.getById(id);
        Content content = new Content();
        Button delete = new Button();
        Field name = new Field(), description = new Field();
        content.setPageName("Жанр у книги");
        delete.setPosition(2);
        delete.setTitle("Удалить");
        delete.setLink(new Link(HttpMethod.DELETE, "/book/" + bookId + "/genres/" + genre.getId()));
        content.setManagement(List.of(delete));
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