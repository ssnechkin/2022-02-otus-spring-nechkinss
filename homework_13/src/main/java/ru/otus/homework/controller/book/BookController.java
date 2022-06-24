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
import ru.otus.homework.service.book.BookUiService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController implements MenuItems {

    private final BookService service;
    private final BookUiService bookUiService;

    public BookController(BookService service, BookUiService bookUiService) {
        this.service = service;
        this.bookUiService = bookUiService;
    }

    @Override
    public List<Button> getMenu() {
        return bookUiService.getMenu();
    }

    @GetMapping("/book")
    public Content list() {
        return new Content().setPageName("Книги")
                .setManagement(getManagementFormBookAdd())
                .setTable(getTableAll());
    }

    @GetMapping("/book/{id}")
    public Content view(@PathVariable("id") long id) {
        return getContentView(id);
    }

    @GetMapping("/book/edit/{id}")
    public Content edit(@PathVariable("id") long id) {
        Book book = service.getById(id);
        return new Content()
                .setPageName("Книги-редактирование")
                .setManagement(List.of(
                        new Button().setTitle("Сохранить")
                                .setLink(new Link().setMethod(HttpMethod.PUT).setValue("/book/" + book.getId()))
                ))
                .setForm(new Form().setFields(List.of(
                        new Field().setType(FieldType.INPUT)
                                .setLabel("Наименование")
                                .setName("name")
                                .setValue(book.getName())
                )));
    }

    @PutMapping("/book/{id}")
    public Content save(@PathVariable("id") long id,
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
        Button add = new Button();
        Field name = new Field();
        content.setPageName("Книга - добавление");
        add.setLink(new Link(HttpMethod.POST, "/book"));
        add.setTitle("Добавить");
        content.setManagement(List.of(add));
        name.setType(FieldType.INPUT);
        name.setLabel("Наименование");
        name.setName("name");
        form.setFields(List.of(name));
        content.setForm(form);
        return content;
    }

    @PostMapping("/book")
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

    @DeleteMapping("/book/{id}")
    public Content delete(@PathVariable("id") long id) {
        Notification notification = new Notification();
        Content content = new Content();
        Book book = service.getById(id);
        Button add = new Button();
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
            rows.add(new Row()
                    .setLink(new Link().setMethod(HttpMethod.GET).setValue("/book/" + book.getId()))
                    .setColumns(List.of(book.getName(), getAuthorsLine(book), getGenresLine(book)))
            );
        }
        table.setRows(rows);
        return table;
    }

    private Content getContentView(long id) {
        Book book = service.getById(id);
        Content content = new Content();
        Field name = new Field(), authors = new Field(), genres = new Field();
        content.setPageName("Книга");
        content.setManagement(getContentViewManagement(book.getId()));
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

    private List<Button> getManagementFormBookAdd() {
        return bookUiService.getManagementFormBookAdd();
    }

    private List<Button> getContentViewManagement(long bookId) {
        List<Button> result = new ArrayList<>();
        result.addAll(bookUiService.getButtonEdit(bookId));
        result.addAll(bookUiService.getButtonDelete(bookId));
        result.addAll(bookUiService.getButtonComments(bookId));
        result.addAll(bookUiService.getButtonAuthors(bookId));
        result.addAll(bookUiService.getButtonGenres(bookId));
        return result;
    }

}