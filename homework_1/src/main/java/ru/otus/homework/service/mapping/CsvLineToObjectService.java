package ru.otus.homework.service.mapping;

public interface CsvLineToObjectService {
    Object convert(String[] line);
}
