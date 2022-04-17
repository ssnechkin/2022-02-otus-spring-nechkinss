package ru.otus.homework.repository.ext;

public interface InsertDao<T> {
    long insert(T object);
}
