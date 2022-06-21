package ru.otus.homework.service.genre;

import ru.otus.homework.dto.out.content.Button;

import java.util.List;

public interface GenreUiService {

    List<Button> getMenu();

    List<Button> getManagementFormAdd();
}
