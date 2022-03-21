package ru.otus.homework.service;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import ru.otus.homework.config.ApplicationConfig;
import ru.otus.homework.config.IOServiceStreams;
import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ApplicationRunner implements TestingRunner {
    private final IOServiceStreams ioService;
    private final QuestionGeneratorService generatorService;
    private final int totalQuestions;
    private final AnswerAnalyzerService answerAnalyzerService;
    private final MessageSource messageSource;
    private final List<Locale> localeList;
    private Locale locale;
    private String lastFirstName;

    public ApplicationRunner(IOServiceStreams ioService, QuestionGeneratorService generatorService,
                             AnswerAnalyzerService answerAnalyzerService, MessageSource messageSource,
                             ApplicationConfig applicationConfig) {
        this.ioService = ioService;
        this.generatorService = generatorService;
        this.answerAnalyzerService = answerAnalyzerService;
        this.messageSource = messageSource;
        totalQuestions = generatorService.getTotalQuestions();

        localeList = new ArrayList<>();
        for (Map.Entry<String, String> entry : applicationConfig.getCsvFileLocalePath().entrySet()) {
            localeList.add(Locale.forLanguageTag(entry.getKey()));
        }
        locale = localeList.get(0);
    }

    public void run() {
        outPutLanguage();
        readLocale();
        inputLastFirstName();
        startTesting();
    }

    @Override
    public void setLastFirstName(String lastFirstName) {
        this.lastFirstName = lastFirstName;
    }

    @Override
    public void readLocale() {
        try {
            locale = localeList.get(Integer.parseInt(ioService.readStringWithPrompt(null)) - 1);
            generatorService.setLocale(locale);
        } catch (Exception e) {
            ioService.outputString("ERROR: You have entered a non-existent value");
        }
    }

    @Override
    public void startTesting() {
        Question question;
        String[] answer;
        printInit();
        ioService.outputString(getStringByLocal("TotalQuestions", new Object[]{totalQuestions}));
        ioService.outputString("");
        for (int i = 0; i < totalQuestions; i++) {
            question = generatorService.getNextQuestion();
            printQuestion(question);
            answer = ioService.readStringWithPrompt(
                    getStringByLocal("EnterTheResponseNumbersSeparatedByASpace", new Object[]{totalQuestions})
            ).split(" ");
            ioService.outputString(getStringByLocal("TheAnswerIs", null)
                    + (answerAnalyzerService.isPassed(question, answer) ? "" : getStringByLocal("not", null))
                    + getStringByLocal("correct", null));
            ioService.outputString("");
        }
        printTestResult();
    }

    @Override
    public void outPutLanguage() {
        for (int i = 1; i <= localeList.size(); i++) {
            ioService.outputString(i + " "
                    + messageSource.getMessage("MyLanguage", null, localeList.get(i - 1)));
        }
    }

    private void inputLastFirstName() {
        if (lastFirstName == null) {
            lastFirstName = ioService.readStringWithPrompt(
                    getStringByLocal("EnterYourLastNameFirstName", null));
        }
    }

    private String getStringByLocal(String text, Object[] textArgs) {
        try {
            return messageSource.getMessage(text, textArgs, locale);
        } catch (NoSuchMessageException e) {
            e.printStackTrace();
            return text;
        }
    }

    private void printInit() {
        ioService.outputString("-------------------------------------------------");
        ioService.outputString("             " + getStringByLocal("TestingStudents", null));
        ioService.outputString("-------------------------------------------------");
    }

    private void printTestResult() {
        ioService.outputString("-------------------------------------------");
        ioService.outputString(lastFirstName + " " + getStringByLocal("Result", null) + " "
                + (answerAnalyzerService.isTestPassed() ? "" : getStringByLocal("TestNot", null))
                + getStringByLocal("passed", null));
    }

    private void printQuestion(Question question) {
        ioService.outputString(question.getQuestion());
        ioService.outputString("------------");
        int questionNumber = 0;
        for (String answer : question.getAnswerOptions()) {
            if (answer != null && answer.length() > 0) {
                ioService.outputString(++questionNumber + " " + answer);
            }
        }
    }
}
