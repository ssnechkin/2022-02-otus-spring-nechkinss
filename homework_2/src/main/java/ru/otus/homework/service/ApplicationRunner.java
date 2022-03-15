package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.config.IOServiceStreams;
import ru.otus.homework.domain.Question;

@Service
public class ApplicationRunner {
    private final IOServiceStreams ioService;
    private final QuestionGeneratorService generatorService;
    private final int totalQuestions;
    private final AnswerAnalyzerService answerAnalyzerService;
    private String lastFirstName;

    public ApplicationRunner(IOServiceStreams ioService, QuestionGeneratorService generatorService,
                             AnswerAnalyzerService answerAnalyzerService) {
        this.ioService = ioService;
        this.generatorService = generatorService;
        this.answerAnalyzerService = answerAnalyzerService;
        totalQuestions = generatorService.getTotalQuestions();
    }

    public void run() {
        Question question;
        String[] answer;
        printInit();
        lastFirstName = ioService.readStringWithPrompt("Enter your last name first name: ");
        ioService.outputString("Total questions: " + totalQuestions);
        for (int i = 0; i < totalQuestions; i++) {
            question = generatorService.getNextQuestion();
            printQuestion(question);
            answer = ioService.readStringWithPrompt("Enter the response numbers separated by a space: ")
                    .split(" ");
            ioService.outputString("The answer is "
                    + (answerAnalyzerService.isPassed(question, answer) ? "" : "not ") + "correct");
            ioService.outputString("");
        }
        printTestResult();
    }

    private void printInit() {
        ioService.outputString("-------------------------------------------");
        ioService.outputString("------------- Testing students ------------");
        ioService.outputString("-------------------------------------------");
    }

    private void printTestResult() {
        ioService.outputString("-------------------------------------------");
        ioService.outputString(lastFirstName + " Result: "
                + (answerAnalyzerService.isTestPassed() ? "" : "Test not ") + "passed!");
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
