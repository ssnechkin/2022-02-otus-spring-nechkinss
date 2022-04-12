package ru.otus.homework.repository.ext;

public interface GetByIdDao<T> {
    T getById(long id);
}
