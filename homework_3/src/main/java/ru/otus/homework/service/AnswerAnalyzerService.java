package ru.otus.homework.service;

import ru.otus.homework.domain.Question;

public interface AnswerAnalyzerService {
    boolean isPassed(Question question, String[] answer);

    boolean isTestPassed();
}
