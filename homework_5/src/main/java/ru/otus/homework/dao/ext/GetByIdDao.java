package ru.otus.homework.dao.ext;

import java.util.List;

public interface GetByIdDao<T> {
    List<T> getById(long id);
}
