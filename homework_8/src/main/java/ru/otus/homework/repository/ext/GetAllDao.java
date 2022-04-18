package ru.otus.homework.repository.ext;

import java.util.List;

public interface GetAllDao<T> {
    List<T> getAll();
}
