package ru.otus.homework.controller.book;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.book.BookComment;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.domain.ui.FieldEntity;
import ru.otus.homework.domain.ui.LinkHref;
import ru.otus.homework.domain.ui.OptionItem;
import ru.otus.homework.domain.ui.RowItem;
import ru.otus.homework.dto.book.BookCommentDto;
import ru.otus.homework.dto.book.BookGenreDto;
import ru.otus.homework.service.UiService;
import ru.otus.homework.service.UiServiceImpl;
import ru.otus.homework.service.book.BookService;
import ru.otus.homework.service.genre.GenreService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookGenresController {

    private final BookService service;
    private final GenreService genreService;
    private final UiService uiService;

    public BookGenresController(BookService service, GenreService genreService, UiService uiService) {
        this.service = service;
        this.genreService = genreService;
        this.uiService = uiService;
    }

    @GetMapping("/book/genres/list")
    public String list(Model model, @RequestParam("id") long id) {
        return rList(id, model);
    }

    @GetMapping("/book/genres/view")
    public String view(Model model, @RequestParam("id") long id,
                       @RequestParam("book_id") long bookId) {
        return rView(bookId, id, model);
    }

    @GetMapping("/book/genres/add")
    public String add(Model model, @RequestParam("book_id") long bookId) {
       List<OptionItem> optionItems = new ArrayList<>();
        for(Genre genre: genreService.getAll()){
            optionItems.add(new OptionItem(genre.getId(), genre.getName()));
        }
        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_SELECT, "Жанр", "genre", optionItems));
        return uiService.add(model, "Жанр в книге - добавление", fields, "/book/genres/create?book_id=" + bookId);
    }

    @PostMapping("/book/genres/create")
    public String create(@ModelAttribute("object") BookGenreDto bookGenreDto,
                         @RequestParam("book_id") long bookId,
                         Model model) {
        Book book = service.getById(bookId);
        Genre genre = genreService.getById(bookGenreDto.getGenre());
        service.addGenre(book, genre);
        return rView(bookId, genre.getId(), model);
    }

    @GetMapping("/book/genres/delete")
    public String delete(@RequestParam("book_id") long bookId,
                         @RequestParam("id") long id,
                         Model model) {
        Book book = service.getById(bookId);
        Genre genre = genreService.getById(id);
        service.deleteGenre(book, genre);
        return rList(bookId, model);
    }

    private String rList(long bookId, Model model) {
        List<String> headerColumns = new ArrayList<>();
        headerColumns.add("Жанр");
        List<RowItem> rows = new ArrayList<>();
        for (Genre genre : service.getById(bookId).getGenres()) {
            List<String> row = new ArrayList<>();
            row.add(genre.getName());
            rows.add(new RowItem("/book/genres/view?id=" + genre.getId() + "&book_id=" + bookId, row));
        }
        return uiService.list(model, "Жанры у книги", "/book/genres/add?book_id=" + bookId, headerColumns, rows);
    }

    private String rView(long bookId, long id, Model model) {
        Genre genre = genreService.getById(id);
        List<LinkHref> links = new ArrayList<>();
        links.add(new LinkHref("Удалить", "/book/genres/delete?book_id=" + bookId + "&id=" + id));
        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Наименование", "name", genre.getName()));
        return uiService.view(model, "Жанр у книги", links, fields);
    }
}