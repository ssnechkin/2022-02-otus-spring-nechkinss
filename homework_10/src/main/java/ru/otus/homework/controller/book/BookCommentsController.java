package ru.otus.homework.controller.book;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.book.BookComment;
import ru.otus.homework.dto.in.book.BookCommentDto;
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
public class BookCommentsController {

    private final BookService service;

    public BookCommentsController(BookService service) {
        this.service = service;
    }

    @GetMapping("/book/comments/list")
    public Content list(@RequestParam("book_id") long bookId) {
        Content content = new Content();
        Menu add = new Menu();
        add.setTitle("Добавить Комментарий");
        add.setLink(new Link(HttpMethod.GET, "/book/comments/add?book_id=" + bookId));
        content.setPageName("Комментарии у книги");
        content.setManagement(List.of(add));
        content.setTable(getTableAll(bookId));
        return content;
    }

    @GetMapping("/book/comments/view")
    public Content view(@RequestParam("id") long id,
                        @RequestParam("book_id") long bookId) {
        return getContentView(bookId, id);
    }

    @GetMapping("/book/comments/add")
    public Content add(@RequestParam("book_id") long bookId) {
        Content content = new Content();
        Form form = new Form();
        Menu add = new Menu();
        Field name = new Field();
        content.setPageName("Комментарий у книги - добавление");
        add.setLink(new Link(HttpMethod.POST, "/book/comments/create?book_id=" + bookId));
        add.setTitle("Добавить");
        content.setManagement(List.of(add));
        name.setType(FieldType.INPUT);
        name.setLabel("Комментарий");
        name.setName("comment");
        form.setFields(List.of(name));
        content.setForm(form);
        return content;
    }

    @PostMapping("/book/comments/create")
    public Content create(@RequestBody BookCommentDto bookCommentDto,
                          @RequestParam("book_id") long bookId) {
        Notification notification = new Notification();
        Content content;
        Book book = service.getById(bookId);
        BookComment bookComment = service.addComment(book, bookCommentDto.getComment());
        content = getContentView(bookId, bookComment.getId());
        notification.setType(NotificationType.INFO);
        notification.setMessage("Комментарий добавлен к книге");
        content.setNotifications(List.of(notification));
        return content;
    }

    @DeleteMapping("/book/comments/delete")
    public Content delete(@RequestParam("book_id") long bookId,
                          @RequestParam("id") long id) {
        Notification notification = new Notification();
        Content content = new Content();
        BookComment bookComment = service.getBookCommentById(id);
        service.deleteComment(bookComment);
        Menu add = new Menu();
        content.setPageName("Комментарии у книги");
        add.setTitle("Добавить");
        add.setLink(new Link(HttpMethod.GET, "/book/comments/add?book_id=" + bookId));
        content.setManagement(List.of(add));
        content.setTable(getTableAll(bookId));
        notification.setType(NotificationType.INFO);
        notification.setMessage("Комментарий удален у книги");
        content.setNotifications(List.of(notification));
        return content;
    }

    private Table getTableAll(long bookId) {
        Table table = new Table();
        table.setLabels(List.of("Комментарий"));
        List<Row> rows = new ArrayList<>();
        for (BookComment bookComment : service.getById(bookId).getComments()) {
            Row row = new Row();
            row.setLink(new Link(HttpMethod.GET, "/book/comments/view?book_id=" + bookId + "&id=" + bookComment.getId()));
            row.setColumns(List.of(bookComment.getComment()));
            rows.add(row);
        }
        table.setRows(rows);
        return table;
    }

    private Content getContentView(long bookId, long id) {
        BookComment bookComment = service.getBookCommentById(id);
        Content content = new Content();
        Menu delete = new Menu();
        Field comment = new Field();
        content.setPageName("Комментарий у книги");
        delete.setPosition(2);
        delete.setTitle("Удалить");
        delete.setLink(new Link(HttpMethod.DELETE, "/book/comments/delete?book_id=" + bookId + "&id=" + bookComment.getId()));
        content.setManagement(List.of(delete));
        comment.setType(FieldType.INPUT);
        comment.setLabel("Наименование");
        comment.setName("name");
        comment.setValue(bookComment.getComment());
        content.setFields(List.of(comment));
        return content;
    }
}