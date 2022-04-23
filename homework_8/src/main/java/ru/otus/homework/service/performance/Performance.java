package ru.otus.homework.service.performance;

import java.util.List;

public interface Performance {

    List<String> getAll();

    String deleteById(String id);
}
