package ru.otus.homework.service.author;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Link;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorUiServiceImpl implements AuthorUiService {

    @Override
    @PostFilter("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public List<Button> getMenu() {
        List<Button> menu = new ArrayList<>();
        menu.add(new Button().setTitle("Авторы")
                .setPosition(3)
                .setLink(new Link().setMethod(HttpMethod.GET).setValue("/author"))
                .setAlt(true)
        );
        return menu;
    }

    @Override
    @PostFilter("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public List<Button> getManagementFormAdd() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button().setTitle("Добавить")
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/author/add")
                ));
        return buttons;
    }
}
