package ru.otus.homework.service.performance.author;

import ru.otus.homework.service.performance.Performance;

public interface AuthorPerformance extends Performance {

    String add(String surname, String name, String patronymic);

    String edit(String id, String surname, String name, String patronymic);
}
