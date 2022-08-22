package ru.dpec.service.ui.handbook;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.dpec.domain.dto.out.Content;
import ru.dpec.domain.dto.out.content.Form;
import ru.dpec.domain.dto.out.enums.Color;
import ru.dpec.domain.dto.out.content.Button;
import ru.dpec.domain.dto.out.content.Link;
import ru.dpec.domain.dto.out.content.table.Table;

import java.util.ArrayList;
import java.util.List;

@Service
public class HandbookUiServiceImpl implements HandbookUiService {

    private final static String PAGE_NAME = "Справочники";

    @Override
    public Content list() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button().setTitle("Типы документов")
                .setColor(Color.cyan)
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/handbook/document_type")
                ));
        buttons.add(new Button().setTitle("Области доступа")
                .setColor(Color.cyan)
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/handbook/scope")
                ));
        buttons.add(new Button().setTitle("Цели")
                .setColor(Color.cyan)
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/handbook/purposes")
                ));
        buttons.add(new Button().setTitle("Действия")
                .setColor(Color.cyan)
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/handbook/actions")
                ));
        return new Content().setPageName(PAGE_NAME)
                .setManagement(buttons)
                .setTable(new Table())
                .setForm(new Form());
    }
}