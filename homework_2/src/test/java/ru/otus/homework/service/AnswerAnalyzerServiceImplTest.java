package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс AnswerAnalyzerServiceImplTest")
class AnswerAnalyzerServiceImplTest {
    @DisplayName("Полложительный тест")
    @Test
    void passedTest() {
        AnswerAnalyzerServiceImpl answerAnalyzerService = new AnswerAnalyzerServiceImpl(5, 1);
        List<Integer> correctAnswers = new ArrayList<>();
        correctAnswers.add(1);
        correctAnswers.add(3);
        List<String> answerOptions = new ArrayList<>();
        answerOptions.add("answer1");
        answerOptions.add("answer2");
        answerOptions.add("answer3");
        answerOptions.add("answer4");
        answerOptions.add("answer5");
        Question question = new Question("Question1", correctAnswers, answerOptions);
        String[] answers = new String[]{"1", "3"};
        assertTrue(answerAnalyzerService.isPassed(question, answers));
    }

    @DisplayName("Отрицательный тест без ответов")
    @Test
    void notPassedTestWithoutAnswers() {
        AnswerAnalyzerServiceImpl answerAnalyzerService = new AnswerAnalyzerServiceImpl(5, 1);
        List<Integer> correctAnswers = new ArrayList<>();
        List<String> answerOptions = new ArrayList<>();
        answerOptions.add("answer1");
        answerOptions.add("answer2");
        answerOptions.add("answer3");
        answerOptions.add("answer4");
        answerOptions.add("answer5");
        Question question = new Question("Question1", correctAnswers, answerOptions);
        String[] answers = new String[]{"1", "3"};
        assertFalse(answerAnalyzerService.isPassed(question, answers));
    }
    @DisplayName("Отрицательный тест с ответами")
    @Test
    void notPassedTest() {
        AnswerAnalyzerServiceImpl answerAnalyzerService = new AnswerAnalyzerServiceImpl(5, 1);
        List<Integer> correctAnswers = new ArrayList<>();
        correctAnswers.add(2);
        correctAnswers.add(3);
        List<String> answerOptions = new ArrayList<>();
        answerOptions.add("answer1");
        answerOptions.add("answer2");
        answerOptions.add("answer3");
        answerOptions.add("answer4");
        answerOptions.add("answer5");
        Question question = new Question("Question1", correctAnswers, answerOptions);
        String[] answers = new String[]{"1", "3"};
        assertFalse(answerAnalyzerService.isPassed(question, answers));
    }
}