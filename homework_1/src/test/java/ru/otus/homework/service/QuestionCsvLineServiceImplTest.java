package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Класс QuestionCsvLineServiceImplTest")
class QuestionCsvLineServiceImplTest {
    @DisplayName("выполняется вывод теста без Exception")
    @Test
    void PrintOneQuestion() {
        List<String> answerOption = new ArrayList<>();

        answerOption.add("answer1");
        answerOption.add("answer2");

        Question question = new Question("Question1", answerOption);

        CsvLineService csvLineService = new QuestionCsvLineServiceImpl();

        try {
            csvLineService.processAline(question);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertNull(e);
        }
    }
}