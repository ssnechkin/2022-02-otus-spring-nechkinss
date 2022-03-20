package ru.otus.homework.domain;

import java.util.List;

public class Question {
    private final String question;
    private final List<Integer> correctAnswers;
    private final List<String> answerOptions;

    public Question(String question, List<Integer> correctAnswers, List<String> answerOptions) {
        this.question = question;
        this.correctAnswers = correctAnswers;
        this.answerOptions = answerOptions;
    }

    public String getQuestion() {
        return question;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public List<String> getAnswerOptions() {
        return answerOptions;
    }

}
