package ru.otus.homework.service.author;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Link;
import ru.otus.homework.repository.MenuRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorUiServiceImpl implements AuthorUiService {

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
