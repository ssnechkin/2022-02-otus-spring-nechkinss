package ru.otus.homework.repository.ext;

public interface GetByIdDao<T> {
    T getById(String id);
}
