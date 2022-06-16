package ru.otus.homework.service.author;

import org.springframework.security.access.prepost.PostAuthorize;
import ru.otus.homework.dto.out.content.Button;

import java.util.List;

public interface AuthorUiService {
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    List<Button> getManagementFormAdd();
}
