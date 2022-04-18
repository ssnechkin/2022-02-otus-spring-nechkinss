package ru.otus.homework.service.ext;

public interface Performance<T> {
    void delete(String id);

    void add(String id);

    void total(long count);

    void notFound(String id);

    void output(T object);
}
