package ru.otus.homework.service.author;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Link;

import java.util.List;

@Service
public class AuthorUiServiceImpl implements AuthorUiService {
    @Override
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public List<Button> getManagementFormAdd() {
        return List.of(new Button().setTitle("Добавить")
                .setLink(new Link().setMethod(HttpMethod.GET)
                        .setValue("/author/add")
                )
        );
    }
}
