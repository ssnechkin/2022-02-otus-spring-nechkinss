package ru.otus.homework.repository.genre;

import ru.otus.homework.entity.Genre;
import ru.otus.homework.repository.ext.*;

public interface GenreDao extends InsertDao<Genre>, UpdateDao<Genre>, DeleteDao,
        GetByIdDao<Genre>, GetAllDao<Genre> {
}
