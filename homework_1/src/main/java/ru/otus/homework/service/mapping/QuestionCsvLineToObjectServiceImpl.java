package ru.otus.homework.service.mapping;

import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionCsvLineToObjectServiceImpl implements CsvLineToObjectService {

    @Override
    public Question convert(String[] line) {
        List<String> answerOptions = new ArrayList<>(Arrays.asList(line).subList(1, line.length));

        return new Question(line[0], answerOptions);
    }
}
