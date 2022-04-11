package ru.otus.homework.repository.genre;

import ru.otus.homework.repository.ext.*;
import ru.otus.homework.entity.Genre;

public interface GenreDao extends CountDao,
        InsertDao<Genre>, UpdateDao<Genre>, DeleteDao,
        GetByIdDao<Genre>, GetAllDao<Genre> {
}
