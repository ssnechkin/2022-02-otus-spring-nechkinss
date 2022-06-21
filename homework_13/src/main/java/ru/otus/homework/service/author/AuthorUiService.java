package ru.otus.homework.service.author;

import ru.otus.homework.dto.out.content.Button;

import java.util.List;

public interface AuthorUiService {

    List<Button> getMenu();

    List<Button> getManagementFormAdd();
}
