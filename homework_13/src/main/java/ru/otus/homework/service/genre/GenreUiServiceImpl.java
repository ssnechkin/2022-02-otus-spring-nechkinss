package ru.otus.homework.service.genre;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Link;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreUiServiceImpl implements GenreUiService {
    @Override
    @PostFilter("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public List<Button> getMenu() {
        List<Button> menu = new ArrayList<>();
        menu.add(new Button().setTitle("Жанры")
                .setPosition(2)
                .setLink(new Link().setMethod(HttpMethod.GET).setValue("/genre"))
                .setAlt(true)
        );
        return menu;
    }

    @Override
    @PostFilter("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public List<Button> getManagementFormAdd() {
        List<Button> books = new ArrayList<>();
        books.add(new Button().setTitle("Добавить")
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/genre/add")
                ));
        return books;
    }
}
