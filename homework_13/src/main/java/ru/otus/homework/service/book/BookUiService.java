package ru.otus.homework.service.book;

import ru.otus.homework.dto.out.content.Button;

import java.util.List;

public interface BookUiService {

    List<Button> getManagementFormBookAdd();

    List<Button> getButtonEdit(long bookId);

    List<Button> getButtonDelete(long bookId);

    List<Button> getButtonComments(long bookId);

    List<Button> getButtonAuthors(long bookId);

    List<Button> getButtonGenres(long bookId);
}
