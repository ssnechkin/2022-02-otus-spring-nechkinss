package ru.otus.homework.controller.author;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.controller.MenuEntity;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.ui.FieldEntity;
import ru.otus.homework.domain.ui.LinkHref;
import ru.otus.homework.domain.ui.MenuItem;
import ru.otus.homework.domain.ui.RowItem;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.service.UiService;
import ru.otus.homework.service.UiServiceImpl;
import ru.otus.homework.service.author.AuthorService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthorController implements MenuEntity {

    private final AuthorService service;
    private final UiService uiService;

    public AuthorController(AuthorService service, UiService uiService) {
        this.service = service;
        this.uiService = uiService;
    }

    @Override
    public List<MenuItem> getMenuList() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem(2, "Авторы", "/author/list"));
        return list;
    }

    @GetMapping("/author/list")
    public String list(Model model) {
        return rList(model);
    }

    @GetMapping("/author/view")
    public String view(Model model, @RequestParam("id") long id) {
        return rView(id, model);
    }

    @GetMapping("/author/edit")
    public String edit(Model model, @RequestParam("id") long id) {
        Author author = service.getById(id);
        if (author != null) {
            List<FieldEntity> fields = new ArrayList<>();
            fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Фамилия", "surname", author.getSurname()));
            fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Имя", "name", author.getName()));
            fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Отчество", "patronymic", author.getPatronymic()));
            return uiService.edit(model, "Автор-редактирование", fields, "/author/save?id=" + author.getId());
        } else {
            return null;
        }
    }

    @PostMapping("/author/save")
    public String save(@RequestParam("id") long id,
                       @ModelAttribute("object") AuthorDto authorDto,
                       Model model) {
        Author author = service.getById(id);
        author = service.edit(author, authorDto.getSurname(), authorDto.getName(), authorDto.getPatronymic());
        return rView(id, model);
    }

    @GetMapping("/author/add")
    public String add(Model model) {
        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Фамилия", "surname", null));
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Имя", "name", null));
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Отчество", "patronymic", null));
        return uiService.add(model, "Атор - добавление", fields, "/author/create");
    }

    @PostMapping("/author/create")
    public String create(@ModelAttribute("object") AuthorDto authorDto,
                         Model model) {
        Author author = service.add(authorDto.getSurname(), authorDto.getName(), authorDto.getPatronymic());
        return rView(author.getId(), model);
    }

    @GetMapping("/author/delete")
    public String delete(@RequestParam("id") long id, Model model) {
        Author author = service.getById(id);
        service.delete(author);
        return rList(model);
    }

    private String rList(Model model) {
        List<String> headerColumns = new ArrayList<>();
        headerColumns.add("Фамилия");
        headerColumns.add("Имя");
        headerColumns.add("Отчество");
        List<RowItem> rows = new ArrayList<>();
        for (Author author : service.getAll()) {
            List<String> row = new ArrayList<>();
            row.add(author.getSurname());
            row.add(author.getName());
            row.add(author.getPatronymic());
            rows.add(new RowItem("/author/view?id=" + author.getId(), row));
        }
        return uiService.list(model, "Авторы", "/author/add", headerColumns, rows);
    }

    private String rView(long id, Model model) {
        Author author = service.getById(id);
        List<LinkHref> links = new ArrayList<>();
        links.add(new LinkHref("Редактировать", "/author/edit?id=" + author.getId()));
        links.add(new LinkHref("Удалить", "/author/delete?id=" + author.getId()));

        List<FieldEntity> fields = new ArrayList<>();
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Фамилия", "surname", author.getSurname()));
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Имя", "name", author.getName()));
        fields.add(new FieldEntity(UiServiceImpl.TYPE_INPUT, "Отчество", "patronymic", author.getPatronymic()));
        return uiService.view(model, "Автор", links, fields);
    }
}