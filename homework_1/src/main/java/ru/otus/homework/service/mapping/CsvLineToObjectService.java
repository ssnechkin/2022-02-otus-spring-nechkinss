package ru.otus.homework.service.mapping;

import ru.otus.homework.domain.Question;

public interface CsvLineToObjectService {
    Question convert(String[] line);
}
