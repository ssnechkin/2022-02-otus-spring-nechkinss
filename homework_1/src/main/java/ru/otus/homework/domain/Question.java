package ru.otus.homework.domain;

import java.util.List;

public class Question {
    private final String question;
    private final List<String> answerOptions;

    public Question(String question, List<String> answerOptions) {
        this.question = question;
        this.answerOptions = answerOptions;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswerOptions() {
        return answerOptions;
    }

}
