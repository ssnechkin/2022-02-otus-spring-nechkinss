package ru.otus.homework.service;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.CsvReaderDao;
import ru.otus.homework.domain.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuestionGeneratorServiceImpl implements QuestionGeneratorService {
    private final List<Question> questions;
    private final List<Integer> questionNumbers;

    private int currentQuestionNumber = 0;

    public QuestionGeneratorServiceImpl(CsvReaderDao csvReaderDao,
                                        CsvToQuestionConverter csvToQuestionConverter,
                                        @Value("${totalQuestions}") Integer totalQuestions) throws CsvValidationException, IOException {
        this.questions = csvToQuestionConverter.getQuestionList(csvReaderDao.getLineList());
        questionNumbers = generateAQuestionNumbers(Math.min(questions.size(), totalQuestions));
    }

    @Override
    public Question getNextQuestion() {
        return questions.get(questionNumbers.get(currentQuestionNumber++));
    }

    @Override
    public int getTotalQuestions() {
        return questionNumbers.size();
    }

    private List<Integer> generateAQuestionNumbers(Integer totalNumbers) {
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i < totalNumbers + 1; i++) {
            addNextGenerateNumber(list, totalNumbers, random);
        }
        return list;
    }

    private void addNextGenerateNumber(List<Integer> list, Integer totalNumbers, Random random) {
        int generateQuestionNumber;
        while (list.size() <= totalNumbers) {
            generateQuestionNumber = random.nextInt(0, totalNumbers);
            if (!isExistNumberInList(generateQuestionNumber, list)) {
                list.add(generateQuestionNumber);
                break;
            }
        }
    }

    private boolean isExistNumberInList(Integer number, List<Integer> list) {
        for (Integer questionNumber : list) {
            if (questionNumber.equals(number)) {
                return true;
            }
        }
        return false;
    }
}
