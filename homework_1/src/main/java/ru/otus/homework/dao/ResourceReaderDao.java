package ru.otus.homework.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ru.otus.homework.service.CsvLineService;
import ru.otus.homework.service.mapping.CsvLineToObjectService;

import java.io.IOException;

public interface ResourceReaderDao {
    public void read(CSVReader reader, CsvLineToObjectService csvLineToObjectService, CsvLineService csvLineService) throws CsvValidationException, IOException;
}
