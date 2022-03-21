package ru.otus.homework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "application")
@EnableConfigurationProperties
@Component
public class ApplicationConfig {
    private Integer totalQuestions, passingScore, numberOfPointsForTheCorrectAnswer;
    private Map<String, String> csvFileLocalePath;

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }

    public Integer getNumberOfPointsForTheCorrectAnswer() {
        return numberOfPointsForTheCorrectAnswer;
    }

    public void setNumberOfPointsForTheCorrectAnswer(Integer numberOfPointsForTheCorrectAnswer) {
        this.numberOfPointsForTheCorrectAnswer = numberOfPointsForTheCorrectAnswer;
    }

    public Map<String, String> getCsvFileLocalePath() {
        return csvFileLocalePath;
    }

    public void setCsvFileLocalePath(Map<String, String> csvFileLocalePath) {
        this.csvFileLocalePath = csvFileLocalePath;
    }
}
