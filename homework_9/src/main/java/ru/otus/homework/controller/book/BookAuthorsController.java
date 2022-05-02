package ru.otus.homework.controller.book;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.ui.FieldEntity;
import ru.otus.homework.domain.ui.LinkHref;
import ru.otus.homework.domain.ui.OptionItem;
import ru.otus.homework.domain.ui.RowItem;
import ru.otus.homework.dto.book.BookAuthorDto;
import ru.otus.homework.service.UiService;
import ru.otus.homework.service.UiServiceImpl;
import ru.otus.homework.service.author.AuthorService;
import ru.otus.homework.service.book.BookService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookAuthorsController {

    private final BookService service;
    private final AuthorService authorService;
    private final UiService uiService;

    public BookAuthorsController(BookService service, AuthorService authorService, UiService uiService) {
        this.service = service;
        this.authorService = authorService;
        this.uiService = uiService;
    }

    @GetMapping("/book/authors/list")
    public String list(Model model, @RequestParam("id") long id) {
        return rList(id, model);
    }

    @GetMapping("/book/authors/view")
    public String view(Model model, @RequestParam("id") long id,
                       @RequestParam("book_id") long bookId) {
        return rView(bookId, id, model);
    }

    @GetMapping("/book/authors/add")
    public String add(Model model, @RequestParam("book_id") long bookId) {
        List<OptionItem> optionItems = new ArrayList<>();
        for (Author author : authorService.getAll()) {
            optionItems.add(new OptionItem(author.getId(), author.getSurname() + " " + author.getName() + " " + author.getPatronymic()));
        }
        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_SELECT, "Автор", "author", optionItems));
        return uiService.add(model, "Автор в книге - добавление", fields, "/book/authors/create?book_id=" + bookId);
    }

    @PostMapping("/book/authors/create")
    public String create(@ModelAttribute("object") BookAuthorDto bookAuthorDto,
                         @RequestParam("book_id") long bookId,
                         Model model) {
        Book book = service.getById(bookId);
        Author author = authorService.getById(bookAuthorDto.getAuthor());
        service.addAuthor(book, author);
        return rView(bookId, author.getId(), model);
    }

    @GetMapping("/book/authors/delete")
    public String delete(@RequestParam("book_id") long bookId,
                         @RequestParam("id") long id,
                         Model model) {
        Book book = service.getById(bookId);
        Author author = authorService.getById(id);
        service.deleteAuthor(book, author);
        return rList(bookId, model);
    }

    private String rList(long bookId, Model model) {
        List<String> headerColumns = new ArrayList<>();
        headerColumns.add("Автор");
        List<RowItem> rows = new ArrayList<>();
        for (Author author : service.getById(bookId).getAuthors()) {
            List<String> row = new ArrayList<>();
            row.add(author.getSurname() + " " + author.getName() + " " + author.getPatronymic());
            rows.add(new RowItem("/book/authors/view?id=" + author.getId() + "&book_id=" + bookId, row));
        }
        return uiService.list(model, "Авторы у книги", "/book/authors/add?book_id=" + bookId, headerColumns, rows);
    }

    private String rView(long bookId, long id, Model model) {
        Author author = authorService.getById(id);
        List<LinkHref> links = new ArrayList<>();
        links.add(new LinkHref("Удалить", "/book/authors/delete?book_id=" + bookId + "&id=" + id));
        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "ФИО", "name", author.getSurname() + " " + author.getName() + " " + author.getPatronymic()));
        return uiService.view(model, "Автор у книги", links, fields);
    }
}