package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.config.ApplicationConfig;
import ru.otus.homework.dao.CsvReaderDao;
import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class QuestionGeneratorServiceImpl implements QuestionGeneratorService {
    private List<Question> questions;
    private List<Integer> questionNumbers;
    private int currentQuestionNumber;
    private final CsvToQuestionConverter csvToQuestionConverter;
    private final CsvReaderDao csvReaderDao;
    private final ApplicationConfig applicationConfig;

    public QuestionGeneratorServiceImpl(CsvReaderDao csvReaderDao,
                                        CsvToQuestionConverter csvToQuestionConverter,
                                        ApplicationConfig applicationConfig) {
        this.csvToQuestionConverter = csvToQuestionConverter;
        this.csvReaderDao = csvReaderDao;
        this.applicationConfig = applicationConfig;
        setLocale(Locale.forLanguageTag("en-EN"));
    }

    @Override
    public Question getNextQuestion() {
        return questions.get(questionNumbers.get(currentQuestionNumber++));
    }

    @Override
    public int getTotalQuestions() {
        return questionNumbers.size();
    }

    @Override
    public void setLocale(Locale locale) {
        this.currentQuestionNumber = 0;
        this.questions = csvToQuestionConverter.getQuestionList(csvReaderDao.getLineList(locale));
        this.questionNumbers = generateAQuestionNumbers(Math.min(questions.size(),
                applicationConfig.getTotalQuestions()));
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
