package ru.otus.homework.controller.book;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/book/{book_id}/comments")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VISITOR')")
    public Content list(@PathVariable("book_id") long bookId) {
        Content content = new Content();
        Button add = new Button();
        add.setTitle("Добавить Комментарий");
        add.setLink(new Link(HttpMethod.GET, "/book/" + bookId + "/comments/add"));
        content.setPageName("Комментарии у книги");
        content.setManagement(List.of(add));
        content.setTable(getTableAll(bookId));
        return content;
    }

    @GetMapping("/book/{book_id}/comments/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VISITOR')")
    public Content view(@PathVariable("id") long id,
                        @PathVariable("book_id") long bookId) {
        return getContentView(bookId, id);
    }

    @GetMapping("/book/{book_id}/comments/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VISITOR')")
    public Content add(@PathVariable("book_id") long bookId) {
        Content content = new Content();
        Form form = new Form();
        Button add = new Button();
        Field name = new Field();
        content.setPageName("Комментарий у книги - добавление");
        add.setLink(new Link(HttpMethod.POST, "/book/" + bookId + "/comments"));
        add.setTitle("Добавить");
        content.setManagement(List.of(add));
        name.setType(FieldType.INPUT);
        name.setLabel("Комментарий");
        name.setName("comment");
        form.setFields(List.of(name));
        content.setForm(form);
        return content;
    }

    @PostMapping("/book/{book_id}/comments")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VISITOR')")
    public Content create(@RequestBody BookCommentDto bookCommentDto,
                          @PathVariable("book_id") long bookId) {
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

    @DeleteMapping("/book/{book_id}/comments/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VISITOR')")
    public Content delete(@PathVariable("book_id") long bookId,
                          @PathVariable("id") long id) {
        Notification notification = new Notification();
        Content content = new Content();
        BookComment bookComment = service.getBookCommentById(id);
        service.deleteComment(bookComment);
        Button add = new Button();
        content.setPageName("Комментарии у книги");
        add.setTitle("Добавить");
        add.setLink(new Link(HttpMethod.GET, "/book/" + bookId + "/comments/add"));
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
            row.setLink(new Link(HttpMethod.GET, "/book/" + bookId + "/comments/" + bookComment.getId()));
            row.setColumns(List.of(bookComment.getComment()));
            rows.add(row);
        }
        table.setRows(rows);
        return table;
    }

    private Content getContentView(long bookId, long id) {
        BookComment bookComment = service.getBookCommentById(id);
        Content content = new Content();
        Button delete = new Button();
        Field comment = new Field();
        content.setPageName("Комментарий у книги");
        delete.setPosition(2);
        delete.setTitle("Удалить");
        delete.setLink(new Link(HttpMethod.DELETE, "/book/" + bookId + "/comments/" + bookComment.getId()));
        content.setManagement(List.of(delete));
        comment.setType(FieldType.INPUT);
        comment.setLabel("Наименование");
        comment.setName("name");
        comment.setValue(bookComment.getComment());
        content.setFields(List.of(comment));
        return content;
    }
}