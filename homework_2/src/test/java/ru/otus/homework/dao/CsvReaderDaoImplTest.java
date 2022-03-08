package ru.otus.homework.dao;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс CsvReaderDaoImplTest")
class CsvReaderDaoImplTest {
    @DisplayName("Чтение csv файла")
    @Test
    void passedTest() throws CsvValidationException, IOException {
        CsvReaderDaoImpl csvReaderDao = new CsvReaderDaoImpl("/questions.csv");
        assertEquals(csvReaderDao.getLineList().size(), 6);
    }
}