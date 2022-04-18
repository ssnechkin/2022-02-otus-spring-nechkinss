package ru.otus.homework.repository.ext;

public interface InsertDao<T> {
    T insert(T object);
}
