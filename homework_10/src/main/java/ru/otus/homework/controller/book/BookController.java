package ru.otus.homework.controller.book;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.controller.MenuItems;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.dto.in.book.BookDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.*;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.dto.out.content.table.Table;
import ru.otus.homework.dto.out.enums.FieldType;
import ru.otus.homework.dto.out.enums.NotificationType;
import ru.otus.homework.service.book.BookService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController implements MenuItems {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @Override
    public List<Menu> getMenu() {
        return List.of(new Menu(1, "Книги", new Link(HttpMethod.GET, "/book/list"), true));
    }

    @GetMapping("/book/list")
    public Content list() {
        Content content = new Content();
        Menu add = new Menu();
        add.setTitle("Добавить");
        add.setLink(new Link(HttpMethod.GET, "/book/add"));
        content.setPageName("Книги");
        content.setManagement(List.of(add));
        content.setTable(getTableAll());
        return content;
    }

    @GetMapping("/book/view")
    public Content view(@RequestParam("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/book/edit")
    public Content edit(@RequestParam("id") long id) {
        Book book = service.getById(id);
        Content content = new Content();
        Form form = new Form();
        Menu save = new Menu();
        Field name = new Field();
        content.setPageName("Книги-редактирование");
        save.setTitle("Сохранить");
        save.setLink(new Link(HttpMethod.PUT, "/book/save?id=" + book.getId()));
        content.setManagement(List.of(save));
        name.setType(FieldType.INPUT);
        name.setLabel("Наименование");
        name.setName("name");
        name.setValue(book.getName());
        form.setFields(List.of(name));
        content.setForm(form);
        return content;
    }

    @PutMapping("/book/save")
    public Content save(@RequestParam("id") long id,
                        @RequestBody BookDto bookDto) {
        Book book = service.getById(id);
        Content content = new Content();
        Notification notification = new Notification();
        if (bookDto.getName() == null || bookDto.getName().isEmpty()) {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Наименование должно быть заполнено");
        } else {
            book = service.editName(book, bookDto.getName());
            content = getContentView(id);
            notification.setType(NotificationType.INFO);
            notification.setMessage("Книга сохранена");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    @GetMapping("/book/add")
    public Content add() {
        Content content = new Content();
        Form form = new Form();
        Menu add = new Menu();
        Field name = new Field();
        content.setPageName("Книга - добавление");
        add.setLink(new Link(HttpMethod.POST, "/book/create"));
        add.setTitle("Добавить");
        content.setManagement(List.of(add));
        name.setType(FieldType.INPUT);
        name.setLabel("Наименование");
        name.setName("name");
        form.setFields(List.of(name));
        content.setForm(form);
        return content;
    }

    @PostMapping("/book/create")
    public Content create(@RequestBody BookDto bookDto) {
        Notification notification = new Notification();
        Content content = new Content();
        if (bookDto.getName() == null || bookDto.getName().isEmpty()) {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Наименование должно быть заполнено");
        } else {
            Book book = service.add(bookDto.getName());
            content = getContentView(book.getId());
            notification.setType(NotificationType.INFO);
            notification.setMessage("Книга добавлена");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    @DeleteMapping("/book/delete")
    public Content delete(@RequestParam("id") long id) {
        Notification notification = new Notification();
        Content content = new Content();
        Book book = service.getById(id);
        Menu add = new Menu();
        service.delete(book);
        content.setPageName("Книги");
        add.setTitle("Добавить");
        add.setLink(new Link(HttpMethod.GET, "/book/add"));
        content.setManagement(List.of(add));
        content.setTable(getTableAll());
        notification.setType(NotificationType.INFO);
        notification.setMessage("Книга удалена");
        content.setNotifications(List.of(notification));
        return content;
    }

    private Table getTableAll() {
        Table table = new Table();
        table.setLabels(List.of("Наименование", "Авторы", "Жанры"));
        List<Row> rows = new ArrayList<>();
        for (Book book : service.getAll()) {
            Row row = new Row();
            row.setLink(new Link(HttpMethod.GET, "/book/view?id=" + book.getId()));
            row.setColumns(List.of(book.getName(), getAuthorsLine(book), getGenresLine(book)));
            rows.add(row);
        }
        table.setRows(rows);
        return table;
    }

    private Content getContentView(long id) {
        Book book = service.getById(id);
        Content content = new Content();
        Menu edit = new Menu(), delete = new Menu(), comments = new Menu(), mAuthors = new Menu(), mGenres = new Menu();
        Field name = new Field(), authors = new Field(), genres = new Field();
        content.setPageName("Книга");
        edit.setPosition(1);
        edit.setTitle("Редактировать");
        edit.setLink(new Link(HttpMethod.GET, "/book/edit?id=" + book.getId()));
        delete.setPosition(2);
        delete.setTitle("Удалить");
        delete.setLink(new Link(HttpMethod.DELETE, "/book/delete?id=" + book.getId()));
        comments.setPosition(2);
        comments.setTitle("Комментарии");
        comments.setLink(new Link(HttpMethod.GET, "/book/comments/list?book_id=" + book.getId()));
        mAuthors.setPosition(2);
        mAuthors.setTitle("Авторы");
        mAuthors.setLink(new Link(HttpMethod.GET, "/book/authors/list?book_id=" + book.getId()));
        mGenres.setPosition(2);
        mGenres.setTitle("Жанры");
        mGenres.setLink(new Link(HttpMethod.GET, "/book/genres/list?book_id=" + book.getId()));
        content.setManagement(List.of(edit, delete, comments, mAuthors, mGenres));
        name.setType(FieldType.INPUT);
        name.setLabel("Наименование");
        name.setName("name");
        name.setValue(book.getName());
        authors.setType(FieldType.INPUT);
        authors.setLabel("Авторы");
        authors.setName("authors");
        authors.setValue(getAuthorsLine(book));
        genres.setType(FieldType.INPUT);
        genres.setLabel("Жанры");
        genres.setName("genres");
        genres.setValue(getGenresLine(book));
        content.setFields(List.of(name, authors, genres));
        return content;
    }

    private String getGenresLine(Book book) {
        StringBuilder line = new StringBuilder();
        for (Genre genre : book.getGenres()) {
            line.append(genre.getName()).append("; ");
        }
        return line.toString();
    }

    private String getAuthorsLine(Book book) {
        StringBuilder line = new StringBuilder();
        for (Author author : book.getAuthors()) {
            line.append(author.getSurname())
                    .append(" ").append(author.getName())
                    .append(" ").append(author.getPatronymic())
                    .append("; ");
        }
        return line.toString();
    }
}