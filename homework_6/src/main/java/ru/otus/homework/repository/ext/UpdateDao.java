package ru.otus.homework.repository.ext;

public interface UpdateDao<T> {
    void update(T object);
}
