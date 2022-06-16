package ru.otus.homework.service.genre;

import org.springframework.security.access.prepost.PostAuthorize;
import ru.otus.homework.dto.out.content.Button;

import java.util.List;

public interface GenreUiService {
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    List<Button> getManagementFormAdd();
}
