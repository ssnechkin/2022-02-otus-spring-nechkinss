package ru.otus.homework.service;

import ru.otus.homework.domain.Question;

import java.util.Locale;

public interface QuestionGeneratorService {
    Question getNextQuestion();

    int getTotalQuestions();

    void rereadQuestions();
}
