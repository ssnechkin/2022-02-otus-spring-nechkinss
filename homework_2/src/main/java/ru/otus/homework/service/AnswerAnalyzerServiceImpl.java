package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Question;

import java.util.List;

@Service
public class AnswerAnalyzerServiceImpl implements AnswerAnalyzerService {
    private final int passingScore;
    private final int numberOfPointsForTheCorrectAnswer;
    private Integer score = 0;

    public AnswerAnalyzerServiceImpl(@Value("${passing-score}") Integer passingScore, @Value("${numberOfPointsForTheCorrectAnswer}") Integer numberOfPointsForTheCorrectAnswer) {
        this.passingScore = passingScore;
        this.numberOfPointsForTheCorrectAnswer = numberOfPointsForTheCorrectAnswer;
    }

    @Override
    public boolean isPassed(Question question, String[] answers) {
        if (question.getCorrectAnswers().size() == answers.length) {
            if (isExistAllAnswer(question.getCorrectAnswers(), answers)) {
                score += numberOfPointsForTheCorrectAnswer;
                return true;
            }
        } else if (question.getCorrectAnswers().size() == 0 && answers.length == 1 && answers[0].length() == 0) {
            score += numberOfPointsForTheCorrectAnswer;
            return true;
        }
        return false;
    }

    private boolean isExistAllAnswer(List<Integer> correctAnswers, String[] answers) {
        for (String answer : answers) {
            if (!isExistAnswer(correctAnswers, answer)) {
                return false;
            }
        }
        return true;
    }

    private boolean isExistAnswer(List<Integer> correctAnswers, String answer) {
        for (Integer correctAnswer : correctAnswers) {
            if (correctAnswer.toString().equals(answer)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isTestPassed() {
        return score >= passingScore;
    }
}