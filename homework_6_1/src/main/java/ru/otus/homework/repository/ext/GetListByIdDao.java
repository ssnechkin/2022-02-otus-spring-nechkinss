package ru.otus.homework.repository.ext;

import java.util.List;

public interface GetListByIdDao<T> {
    List<T> getListById(long id);
}
