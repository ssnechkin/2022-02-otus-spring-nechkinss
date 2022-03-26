package ru.otus.homework.dao;

import java.util.List;
import java.util.Locale;

public interface CsvReaderDao {
    List<String[]> getLineList();
}
