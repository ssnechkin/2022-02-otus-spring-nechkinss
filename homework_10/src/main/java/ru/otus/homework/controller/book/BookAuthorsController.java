package ru.otus.homework.controller.book;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.dto.in.book.BookAuthorDto;
import ru.otus.homework.dto.out.Content;
import ru.otus.homework.dto.out.content.*;
import ru.otus.homework.dto.out.content.table.Row;
import ru.otus.homework.dto.out.content.table.Table;
import ru.otus.homework.dto.out.enums.FieldType;
import ru.otus.homework.dto.out.enums.NotificationType;
import ru.otus.homework.service.author.AuthorService;
import ru.otus.homework.service.book.BookService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookAuthorsController {

    private final BookService service;
    private final AuthorService authorService;

    public BookAuthorsController(BookService service, AuthorService authorService) {
        this.service = service;
        this.authorService = authorService;
    }

    @GetMapping("/book/authors/list")
    public Content list(@RequestParam("book_id") long bookId) {
        Content content = new Content();
        Menu add = new Menu();
        add.setTitle("Добавить автора");
        add.setLink(new Link(HttpMethod.GET, "/book/authors/add?book_id=" + bookId));
        content.setPageName("Авторы у книги");
        content.setManagement(List.of(add));
        content.setTable(getTableAll(bookId));
        return content;
    }

    @GetMapping("/book/authors/view")
    public Content view(@RequestParam("id") long id,
                        @RequestParam("book_id") long bookId) {
        return getContentView(bookId, id);
    }

    @GetMapping("/book/authors/add")
    public Content add(@RequestParam("book_id") long bookId) {
        Content content = new Content();
        Form form = new Form();
        Menu add = new Menu();
        Field name = new Field();
        content.setPageName("Автор у книги - добавление");
        add.setLink(new Link(HttpMethod.POST, "/book/authors/create?book_id=" + bookId));
        add.setTitle("Добавить");
        content.setManagement(List.of(add));
        name.setType(FieldType.SELECT);
        name.setLabel("Автор");
        name.setName("author");
        List<ValueItem> valueItems = new ArrayList<>();
        for (Author author : authorService.getAll()) {
            valueItems.add(new ValueItem(author.getId(),
                    author.getSurname() + " " + author.getName() + " " + author.getPatronymic()));
        }
        name.setValues(valueItems);
        form.setFields(List.of(name));
        content.setForm(form);
        return content;
    }

    @PostMapping("/book/authors/create")
    public Content create(@RequestBody BookAuthorDto bookAuthorDto,
                          @RequestParam("book_id") long bookId) {
        Notification notification = new Notification();
        Content content;
        Book book = service.getById(bookId);
        Author author = authorService.getById(bookAuthorDto.getAuthor());
        service.addAuthor(book, author);
        content = getContentView(bookId, author.getId());
        notification.setType(NotificationType.INFO);
        notification.setMessage("Автор добавлен к книге");
        content.setNotifications(List.of(notification));
        return content;
    }

    @DeleteMapping("/book/authors/delete")
    public Content delete(@RequestParam("book_id") long bookId,
                          @RequestParam("id") long id) {
        Notification notification = new Notification();
        Content content = new Content();
        Book book = service.getById(bookId);
        Author author = authorService.getById(id);
        boolean result = service.deleteAuthor(book, author);
        Menu add = new Menu();
        content.setPageName("Авторы у книги");
        add.setTitle("Добавить");
        add.setLink(new Link(HttpMethod.GET, "/book/authors/add?book_id=" + bookId));
        content.setManagement(List.of(add));
        content.setTable(getTableAll(bookId));
        if (result) {
            notification.setType(NotificationType.INFO);
            notification.setMessage("Автор удален у книги");
        } else {
            notification.setType(NotificationType.WARNING);
            notification.setMessage("Не удалось удалить. Автор отсутствует у книги");
        }
        content.setNotifications(List.of(notification));
        return content;
    }

    private Table getTableAll(long bookId) {
        Table table = new Table();
        table.setLabels(List.of("Фамилия", "Имя", "Отчество"));
        List<Row> rows = new ArrayList<>();
        for (Author author : service.getById(bookId).getAuthors()) {
            Row row = new Row();
            row.setLink(new Link(HttpMethod.GET, "/book/authors/view?book_id=" + bookId + "&id=" + author.getId()));
            row.setColumns(List.of(author.getSurname(), author.getName(), author.getPatronymic() == null ? "" : author.getPatronymic()));
            rows.add(row);
        }
        table.setRows(rows);
        return table;
    }

    private Content getContentView(long bookId, long id) {
        Author author = authorService.getById(id);
        Content content = new Content();
        Menu delete = new Menu();
        Field surname = new Field(), name = new Field(), patronymic = new Field();
        content.setPageName("Автор у книги");
        delete.setPosition(2);
        delete.setTitle("Удалить");
        delete.setLink(new Link(HttpMethod.DELETE, "/book/authors/delete?book_id=" + bookId + "&id=" + author.getId()));
        content.setManagement(List.of(delete));
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