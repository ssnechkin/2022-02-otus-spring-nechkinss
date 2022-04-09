package ru.otus.homework.repository.ext;

import java.util.List;

public interface GetByIdDao<T> {
    List<T> getById(long id);
}
