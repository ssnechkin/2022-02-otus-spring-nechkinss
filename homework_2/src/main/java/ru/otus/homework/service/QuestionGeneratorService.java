package ru.otus.homework.service;

import ru.otus.homework.domain.Question;

public interface QuestionGeneratorService {
    Question getNextQuestion();

    int getTotalQuestions();
}
