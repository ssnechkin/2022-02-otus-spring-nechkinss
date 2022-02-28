package ru.otus.homework.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import ru.otus.homework.service.CsvLineService;
import ru.otus.homework.service.mapping.CsvLineToObjectService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Класс CsvResourceReaderDaoTest")
class CsvResourceReaderDaoTest {
    @DisplayName("Создание бина для чтения ресурса questionsCsv")
    @Test
    void createBeanQuestionsCsv() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        ClassPathResource classPathResource = (ClassPathResource) context.getBean("questionsCsv");
        assertNotNull(classPathResource);
    }

    @DisplayName("Создание бина для чтения ресурса questionCsvLineToObjectService")
    @Test
    void createBeanQuestionCsvLineToObjectService() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        CsvLineToObjectService csvLineToObjectService = (CsvLineToObjectService) context.getBean("questionCsvLineToObjectService");
        assertNotNull(csvLineToObjectService);
    }

    @DisplayName("Создание бина для чтения ресурса questionCsvLineService")
    @Test
    void createBeanQuestionCsvLineService() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        CsvLineService csvLineService = (CsvLineService) context.getBean("questionCsvLineService");
        assertNotNull(csvLineService);
    }

}