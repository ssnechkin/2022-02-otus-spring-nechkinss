package ru.otus.homework.service;

import ru.otus.homework.domain.Question;

public class QuestionCsvLineServiceImpl implements CsvLineService {

    @Override
    public void processAline(Question question) {
        if (question.getQuestion() != null && question.getQuestion().length() > 1) {
            System.out.println(question.getQuestion());
            System.out.println("------------");

            for (String answer : question.getAnswerOptions()) {
                if (answer != null && answer.length() > 0) System.out.println(" - " + answer);
            }

            System.out.println();
        }
    }
}
