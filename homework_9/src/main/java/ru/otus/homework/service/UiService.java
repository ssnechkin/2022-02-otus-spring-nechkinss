package ru.otus.homework.service;

import org.springframework.ui.Model;
import ru.otus.homework.domain.ui.FieldEntity;
import ru.otus.homework.domain.ui.LinkHref;
import ru.otus.homework.domain.ui.RowItem;

import java.util.List;

public interface UiService {
    String list(Model model, String listName, String addLink, List<String> headColumns, List<RowItem> rows);

    String view(Model model, String name, List<LinkHref> links, List<FieldEntity> fields);

    String add(Model model, String name, List<FieldEntity> fields, String createLink);

    String edit(Model model, String name, List<FieldEntity> fields, String saveLink);
}
