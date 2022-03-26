package ru.otus.homework.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.config.ApplicationConfig;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс CsvReaderDaoImplTest")
class CsvReaderDaoImplTest {
    private static ApplicationConfig applicationConfig;

    @BeforeAll
    public static void initApplicationConfig() {
        applicationConfig = new ApplicationConfig();
        applicationConfig.setPassingScore(4);
        applicationConfig.setTotalQuestions(5);
        applicationConfig.setNumberOfPointsForTheCorrectAnswer(1);
        Map<String, String> csvFileMap = new HashMap<>();
        csvFileMap.put("en-EN", "i18n/questions_en_EN.csv");
        applicationConfig.setCsvFileLocalePath(csvFileMap);
    }

    @DisplayName("Чтение csv файла")
    @Test
    void passedTest() {
        CsvReaderDaoImpl csvReaderDao = new CsvReaderDaoImpl(applicationConfig);
        assertEquals(csvReaderDao.getLineList(Locale.forLanguageTag("en-EN")).size(), 10);
    }
}