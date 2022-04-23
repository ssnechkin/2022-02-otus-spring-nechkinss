package ru.otus.homework.service.texter;

public interface Texter<T> {

    String add(T t);

    String total(long count);

    String notFound(String id);

    String delete(String id);
}
