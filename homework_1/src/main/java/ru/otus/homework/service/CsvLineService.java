package ru.otus.homework.service;

import ru.otus.homework.domain.Question;

public interface CsvLineService {
    void processAline(Question question);
}
