package ru.otus.homework.dao.genre;

import ru.otus.homework.dao.ext.*;
import ru.otus.homework.domain.Genre;

public interface GenreDao extends SequenceDao, CountDao,
        InsertDao<Genre>, UpdateDao<Genre>, DeleteDao,
        GetByIdDao<Genre>, GetAllDao<Genre> {
}
