package ru.otus.homework.service.mapping;

import ru.otus.homework.domain.Question;

public class QuestionCsvLineToObjectServiceImpl implements CsvLineToObjectService {

    public Object convert(String[] line) {
        String[] answerOptions = new String[line.length - 1];

        System.arraycopy(line, 1, answerOptions, 0, line.length - 1);

        Question question = new Question();
        question.setQuestion(line[0]);
        question.setAnswerOptions(answerOptions);

        return question;
    }
}
