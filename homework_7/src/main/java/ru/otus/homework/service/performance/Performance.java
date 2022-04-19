package ru.otus.homework.service.performance;

public interface Performance<T> {
    void delete(long id);

    void add(long id);

    void total(long count);

    void notFound(long id);

    void output(T object);

    void edit(long id, T object);
}
