package ru.otus.homework.shell.listener;

import org.springframework.stereotype.Component;
import ru.otus.homework.service.TestingRunner;
import ru.otus.homework.shell.event.ReadLanguageEvent;
import ru.otus.homework.shell.event.TestingStarterEvent;

@Component
public class AppShellEventListener {
    private final TestingRunner testingRunner;

    public AppShellEventListener(TestingRunner testingRunner) {
        this.testingRunner = testingRunner;
    }

    @org.springframework.context.event.EventListener
    public void onApplicationEvent(TestingStarterEvent event) {
        testingRunner.setLastFirstName(event.getShellAttributes().getLastName()
                + " " + event.getShellAttributes().getFirstName());
        testingRunner.startTesting();
    }

    @org.springframework.context.event.EventListener
    public void onApplicationEvent(ReadLanguageEvent readLanguageEvent) {
        testingRunner.outPutLanguage();
        testingRunner.readLocale();
    }
}