package ru.otus.homework.service;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.CsvReaderDao;
import ru.otus.homework.domain.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvToQuestionConverterImpl implements CsvToQuestionConverter {
    private final List<String[]> lineList;

    public CsvToQuestionConverterImpl(CsvReaderDao csvReaderDao) throws CsvValidationException, IOException {
        this.lineList = csvReaderDao.getLineList();
    }

    @Override
    public List<Question> getQuestionList() {
        List<Question> questions = new ArrayList<>();
        for (String[] csvLine : lineList) {
            String question = getQuestion(csvLine);
            List<Integer> correctAnswers = getCorrectAnswers(csvLine);
            List<String> answerOptions = getAnswerOptions(csvLine);
            if (question.length() > 0) {
                questions.add(new Question(question, correctAnswers, answerOptions));
            }
        }
        return questions;
    }

    private String getQuestion(String[] csvLine) {
        return csvLine[0];
    }

    private List<Integer> getCorrectAnswers(String[] csvLine) {
        List<Integer> correctAnswers = new ArrayList<>();
        for (int i = 0; i < getCountCorrectAnswers(csvLine); i++) {
            correctAnswers.add(Integer.parseInt(csvLine[2 + i].trim()));
        }
        return correctAnswers;
    }

    private List<String> getAnswerOptions(String[] csvLine) {
        return new ArrayList<>(Arrays.asList(csvLine).subList(2 + getCountCorrectAnswers(csvLine), csvLine.length));
    }

    private int getCountCorrectAnswers(String[] csvLine) {
        return Integer.parseInt(csvLine[1].trim());
    }
}
