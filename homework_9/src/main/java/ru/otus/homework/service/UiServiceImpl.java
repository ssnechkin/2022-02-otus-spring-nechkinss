package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.otus.homework.domain.ui.FieldEntity;
import ru.otus.homework.domain.ui.LinkHref;
import ru.otus.homework.domain.ui.RowItem;

import java.util.List;

@Service
public class UiServiceImpl implements UiService {
    public static final String TYPE_INPUT = "input";
    public static final String TYPE_SELECT = "select";

    @Override
    public String list(Model model, String listName, String addLink, List<String> headColumns, List<RowItem> rows) {
        model.addAttribute("list_name", listName);
        model.addAttribute("add_link", addLink);
        model.addAttribute("columns", headColumns);
        model.addAttribute("rows", rows);
        return "list";
    }

    @Override
    public String view(Model model, String name, List<LinkHref> links, List<FieldEntity> fields) {
        model.addAttribute("view_name", name);
        model.addAttribute("links", links);
        model.addAttribute("fields", fields);
        return "view";
    }

    @Override
    public String add(Model model, String name, List<FieldEntity> fields, String createLink) {
        model.addAttribute("add_name", name);
        model.addAttribute("fields", fields);
        model.addAttribute("create_link", createLink);
        return "add";
    }

    @Override
    public String edit(Model model, String name, List<FieldEntity> fields, String saveLink) {
        model.addAttribute("edit_name", name);
        model.addAttribute("fields", fields);
        model.addAttribute("save_link", saveLink);
        return "edit";
    }
}
