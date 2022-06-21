package ru.otus.homework.service.book;

import ru.otus.homework.dto.out.content.Button;

import java.util.List;

public interface BookUiService {

    List<Button> getMenu();

    List<Button> getManagementFormBookAdd();

    Button getButtonEdit(long bookId);

    Button getButtonDelete(long bookId);

    Button getButtonComments(long bookId);

    Button getButtonAuthors(long bookId);

    Button getButtonGenres(long bookId);
}
