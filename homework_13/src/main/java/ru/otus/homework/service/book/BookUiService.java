package ru.otus.homework.service.book;

import org.springframework.security.access.prepost.PostAuthorize;
import ru.otus.homework.dto.out.content.Button;

import java.util.List;

public interface BookUiService {

    List<Button> getManagementFormBookAdd();

    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    Button getButtonEdit(long bookId);

    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    Button getButtonDelete(long bookId);

    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR') or hasRole('ROLE_VISITOR')")
    Button getButtonComments(long bookId);

    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    Button getButtonAuthors(long bookId);

    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    Button getButtonGenres(long bookId);
}
