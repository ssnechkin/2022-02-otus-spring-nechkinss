package ru.otus.homework.controller.book;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.controller.MenuEntity;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.domain.ui.FieldEntity;
import ru.otus.homework.domain.ui.LinkHref;
import ru.otus.homework.domain.ui.MenuItem;
import ru.otus.homework.domain.ui.RowItem;
import ru.otus.homework.dto.book.BookDto;
import ru.otus.homework.service.UiService;
import ru.otus.homework.service.UiServiceImpl;
import ru.otus.homework.service.book.BookService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController implements MenuEntity {

    private final BookService service;
    private final UiService uiService;

    public BookController(BookService service, UiService uiService) {
        this.service = service;
        this.uiService = uiService;
    }

    @Override
    public List<MenuItem> getMenuList() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem(1, "Книги", "/book/list"));
        return list;
    }

    @GetMapping("/book/list")
    public String list(Model model) {
        return rList(model);
    }

    @GetMapping("/book/view")
    public String view(Model model, @RequestParam("id") long id) {
        return rView(id, model);
    }

    @GetMapping("/book/edit")
    public String edit(Model model, @RequestParam("id") long id) {
        Book book = service.getById(id);
        if (book != null) {
            List<FieldEntity> fields = new ArrayList<>();
            fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Наименование", "name", book.getName()));
            return uiService.edit(model, "Книга-редактирование", fields, "/book/save?id=" + book.getId());
        } else {
            return null;
        }
    }

    @PostMapping("/book/save")
    public String save(@RequestParam("id") long id,
                       @ModelAttribute("object") BookDto bookDto,
                       Model model) {
        Book book = service.getById(id);
        book = service.editName(book, bookDto.getName());
        return rView(id, model);
    }

    @GetMapping("/book/add")
    public String add(Model model) {
        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Наименование", "name", null));
        return uiService.add(model, "Книга - добавление", fields, "/book/create");
    }

    @PostMapping("/book/create")
    public String create(@ModelAttribute("object") BookDto bookDto,
                         Model model) {
        Book book = service.add(bookDto.getName());
        return rView(book.getId(), model);
    }

    @GetMapping("/book/delete")
    public String delete(@RequestParam("id") long id, Model model) {
        Book book = service.getById(id);
        service.delete(book);
        return rList(model);
    }

    private String rList(Model model) {
        List<String> headerColumns = new ArrayList<>();
        headerColumns.add("Наименование");
        headerColumns.add("Жанры");
        headerColumns.add("Авторы");
        List<RowItem> rows = new ArrayList<>();
        for (Book book : service.getAll()) {
            List<String> row = new ArrayList<>();
            row.add(book.getName());
            row.add(getGenresLine(book));
            row.add(getAuthorsLine(book));
            rows.add(new RowItem("/book/view?id=" + book.getId(), row));
        }
        return uiService.list(model, "Книги", "/book/add", headerColumns, rows);
    }

    private String rView(long id, Model model) {
        Book book = service.getById(id);
        List<LinkHref> links = new ArrayList<>();
        links.add(new LinkHref("Редактировать", "/book/edit?id=" + book.getId()));
        links.add(new LinkHref("Удалить", "/book/delete?id=" + book.getId()));
        links.add(new LinkHref("Комментарии", "/book/comments/list?id=" + book.getId()));
        links.add(new LinkHref("Жанры", "/book/genres/list?id=" + book.getId()));
        links.add(new LinkHref("Авторы", "/book/authors/list?id=" + book.getId()));

        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Наименование", "name", book.getName()));
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Жанры", "genres", getGenresLine(book)));
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Авторы", "authors", getAuthorsLine(book)));
        return uiService.view(model, "Книга", links, fields);
    }

    private String getGenresLine(Book book) {
        StringBuilder line = new StringBuilder();
        for (Genre genre : book.getGenres()) {
            line.append(genre.getName() + "; ");
        }
        return line.toString();
    }

    private String getAuthorsLine(Book book) {
        StringBuilder line = new StringBuilder();
        for (Author author : book.getAuthors()) {
            line.append(author.getSurname() + " " + author.getName() + " " + author.getPatronymic() + "; ");
        }
        return line.toString();
    }
}