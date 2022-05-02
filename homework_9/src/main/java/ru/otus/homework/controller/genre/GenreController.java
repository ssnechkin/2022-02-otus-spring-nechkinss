package ru.otus.homework.controller.genre;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.controller.MenuEntity;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.domain.ui.FieldEntity;
import ru.otus.homework.domain.ui.LinkHref;
import ru.otus.homework.domain.ui.MenuItem;
import ru.otus.homework.domain.ui.RowItem;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.service.UiService;
import ru.otus.homework.service.UiServiceImpl;
import ru.otus.homework.service.genre.GenreService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GenreController implements MenuEntity {

    private final GenreService service;
    private final UiService uiService;

    public GenreController(GenreService service, UiService uiService) {
        this.service = service;
        this.uiService = uiService;
    }

    @Override
    public List<MenuItem> getMenuList() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem(3, "Жанры", "/genre/list"));
        return list;
    }

    @GetMapping("/genre/list")
    public String list(Model model) {
        return rList(model);
    }

    @GetMapping("/genre/view")
    public String view(Model model, @RequestParam("id") long id) {
        return rView(id, model);
    }

    @GetMapping("/genre/edit")
    public String edit(Model model, @RequestParam("id") long id) {
        Genre genre = service.getById(id);
        if (genre != null) {
            List<FieldEntity> fields = new ArrayList<>();
            fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Наименование", "name", genre.getName()));
            fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Описание", "description", genre.getDescription()));
            return uiService.edit(model, "Жанр-редактирование", fields, "/genre/save?id=" + genre.getId());
        } else {
            return null;
        }
    }

    @PostMapping("/genre/save")
    public String save(@RequestParam("id") long id,
                       @ModelAttribute("object") GenreDto genreDto,
                       Model model) {
        Genre genre = service.getById(id);
        genre = service.editName(genre, genreDto.getName());
        genre = service.editDescription(genre, genreDto.getDescription());
        return rView(id, model);
    }

    @GetMapping("/genre/add")
    public String add(Model model) {
        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Наименование", "name", null));
        return uiService.add(model, "Жанр - добавление", fields, "/genre/create");
    }

    @PostMapping("/genre/create")
    public String create(@ModelAttribute("object") GenreDto genreDto,
                         Model model) {
        Genre genre = service.add(genreDto.getName());
        return rView(genre.getId(), model);
    }

    @GetMapping("/genre/delete")
    public String delete(@RequestParam("id") long id, Model model) {
        Genre genre = service.getById(id);
        service.delete(genre);
        return rList(model);
    }

    private String rList(Model model) {
        List<String> headerColumns = new ArrayList<>();
        headerColumns.add("Наименование");
        headerColumns.add("Описание");
        List<RowItem> rows = new ArrayList<>();
        for (Genre genre : service.getAll()) {
            List<String> row = new ArrayList<>();
            row.add(genre.getName());
            row.add(genre.getDescription());
            rows.add(new RowItem("/genre/view?id=" + genre.getId(), row));
        }
        return uiService.list(model, "Жанры", "/genre/add", headerColumns, rows);
    }

    private String rView(long id, Model model) {
        Genre genre = service.getById(id);
        List<LinkHref> links = new ArrayList<>();
        links.add(new LinkHref("Редактировать", "/genre/edit?id=" + genre.getId()));
        links.add(new LinkHref("Удалить", "/genre/delete?id=" + genre.getId()));

        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Наименование", "name", genre.getName()));
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Описание", "description", genre.getDescription()));
        return uiService.view(model, "Жанр", links, fields);
    }
}