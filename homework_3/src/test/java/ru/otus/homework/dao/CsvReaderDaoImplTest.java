package ru.otus.homework.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.config.ApplicationConfig;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс CsvReaderDaoImplTest")
class CsvReaderDaoImplTest {
    @DisplayName("Чтение csv файла")
    @Test
    void passedTest() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setPassingScore(4);
        applicationConfig.setTotalQuestions(5);
        applicationConfig.setNumberOfPointsForTheCorrectAnswer(1);
        Map<String, String> csvFileMap = new HashMap<>();
        csvFileMap.put("en-EN", "i18n/questions_en_EN.csv");
        applicationConfig.setCsvFileLocalePath(csvFileMap);
        CsvReaderDaoImpl csvReaderDao = new CsvReaderDaoImpl(applicationConfig);
        assertEquals(csvReaderDao.getLineList(Locale.forLanguageTag("en-EN")).size(), 10);
    }
}