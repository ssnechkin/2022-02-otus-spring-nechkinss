package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.domain.Question;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Класс QuestionCsvLineServiceImplTest")
class QuestionCsvLineServiceImplTest {
    @DisplayName("выполняется вывод теста без Exception")
    @Test
    void PrintOneQuestion() {
        String[] answerOption = new String[2];
        answerOption[0] = "answer1";
        answerOption[1] = "answer2";

        Question question = new Question();
        question.setQuestion("Question1");
        question.setAnswerOptions(answerOption);

        CsvLineService csvLineService = new QuestionCsvLineServiceImpl();

        try {
            csvLineService.processAline(question);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertNull(e);
        }
    }
}