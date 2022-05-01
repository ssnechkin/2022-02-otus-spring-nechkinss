package ru.otus.homework.controller.book;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.book.BookComment;
import ru.otus.homework.domain.ui.FieldEntity;
import ru.otus.homework.domain.ui.LinkHref;
import ru.otus.homework.domain.ui.RowItem;
import ru.otus.homework.dto.book.BookCommentDto;
import ru.otus.homework.service.UiService;
import ru.otus.homework.service.UiServiceImpl;
import ru.otus.homework.service.book.BookService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookCommentsController {

    private final BookService service;
    private final UiService uiService;

    public BookCommentsController(BookService service, UiService uiService) {
        this.service = service;
        this.uiService = uiService;
    }

    @GetMapping("/book/comments/list")
    public String list(Model model, @RequestParam("id") long id) {
        return rList(id, model);
    }

    @GetMapping("/book/comments/view")
    public String view(Model model, @RequestParam("id") long id,
                       @RequestParam("book_id") long bookId) {
        return rView(bookId, id, model);
    }

    @GetMapping("/book/comments/edit")
    public String edit(Model model, @RequestParam("id") long id,
                       @RequestParam("book_id") long bookId) {
        BookComment bookComment = service.getBookCommentById(id);
        if (bookComment != null) {
            List<FieldEntity> fields = new ArrayList<>();
            fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Комментарий", "name", bookComment.getComment()));
            return uiService.edit(model, "Комментарий-редактирование", fields, "/book/comments/save?id=" + bookComment.getId() + "&book_id=" + bookId);
        } else {
            return null;
        }
    }

    @PostMapping("/book/comments/save")
    public String save(@RequestParam("id") long id,
                       @RequestParam("book_id") long bookId,
                       @ModelAttribute("object") BookCommentDto bookCommentDto,
                       Model model) {
        BookComment comment = service.getBookCommentById(id);
        comment = service.editComment(comment, bookCommentDto.getName());
        return rView(bookId, id, model);
    }

    @GetMapping("/book/comments/add")
    public String add(Model model, @RequestParam("book_id") long bookId) {
        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Комментарий", "name", null));
        return uiService.add(model, "Комментарий - добавление", fields, "/book/comments/create?book_id=" + bookId);
    }

    @PostMapping("/book/comments/create")
    public String create(@ModelAttribute("object") BookCommentDto bookCommentDto,
                         @RequestParam("book_id") long bookId,
                         Model model) {
        Book book = service.getById(bookId);
        BookComment bookComment = service.addComment(book, bookCommentDto.getName());
        return rView(bookId, bookComment.getId(), model);
    }

    @GetMapping("/book/comments/delete")
    public String delete(@RequestParam("book_id") long bookId,
                         @RequestParam("id") long id,
                         Model model) {
        BookComment comment = service.getBookCommentById(id);
        service.deleteComment(comment);
        return rList(bookId, model);
    }

    private String rList(long bookId, Model model) {
        List<String> headerColumns = new ArrayList<>();
        headerColumns.add("Комментарий");
        List<RowItem> rows = new ArrayList<>();
        for (BookComment bookComment : service.getById(bookId).getComments()) {
            List<String> row = new ArrayList<>();
            row.add(bookComment.getComment());
            rows.add(new RowItem("/book/comments/view?id=" + bookComment.getId() + "&book_id=" + bookId, row));
        }
        return uiService.list(model, "Комментарии", "/book/comments/add?book_id=" + bookId, headerColumns, rows);
    }

    private String rView(long bookId, long id, Model model) {
        BookComment bookComment = service.getBookCommentById(id);
        List<LinkHref> links = new ArrayList<>();
        links.add(new LinkHref("Редактировать", "/book/comments/edit?book_id=" + bookId + "&id=" + id));
        links.add(new LinkHref("Удалить", "/book/comments/delete?book_id=" + bookId + "&id=" + id));
        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Комментарий", "name", bookComment.getComment()));
        return uiService.view(model, "Комментарий", links, fields);
    }
}