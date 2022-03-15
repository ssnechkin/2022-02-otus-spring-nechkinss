package ru.otus.homework.dao;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.List;

public interface CsvReaderDao {
    List<String[]> getLineList() throws CsvValidationException, IOException;
}
