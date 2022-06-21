package ru.otus.homework.service.book;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Link;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookUiServiceImpl implements BookUiService {

    @Override
    @PostFilter("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR') or hasRole('ROLE_VISITOR')")
    public List<Button> getMenu() {
        List<Button> menu = new ArrayList<>();
        menu.add(new Button().setTitle("Книги")
                .setPosition(1)
                .setLink(new Link().setMethod(HttpMethod.GET).setValue("/book"))
                .setAlt(true)
        );
        return menu;
    }

    @Override
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public List<Button> getManagementFormBookAdd() {
        return List.of(new Button().setTitle("Добавить")
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/book/add")
                )
        );
    }

    @Override
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public Button getButtonEdit(long bookId) {
        return new Button().setPosition(1).setTitle("Редактировать")
                .setLink(new Link().setMethod(HttpMethod.GET).setValue("/book/edit/" + bookId));
    }

    @Override
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public Button getButtonDelete(long bookId) {
        return new Button().setPosition(2).setTitle("Удалить")
                .setLink(new Link().setMethod(HttpMethod.DELETE).setValue("/book/" + bookId));
    }

    @Override
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VISITOR')")
    public Button getButtonComments(long bookId) {
        return new Button().setPosition(3).setTitle("Комментарии")
                .setLink(new Link().setMethod(HttpMethod.GET).setValue("/book/" + bookId + "/comments"));
    }

    @Override
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public Button getButtonAuthors(long bookId) {
        return new Button().setPosition(4).setTitle("Авторы")
                .setLink(new Link().setMethod(HttpMethod.GET).setValue("/book/" + bookId + "/authors"));
    }

    @Override
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public Button getButtonGenres(long bookId) {
        return new Button().setPosition(4).setTitle("Жанры")
                .setLink(new Link().setMethod(HttpMethod.GET).setValue("/book/" + bookId + "/genres"));
    }
}
