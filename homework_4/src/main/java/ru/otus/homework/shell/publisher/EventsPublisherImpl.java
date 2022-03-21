package ru.otus.homework.shell.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.ShellAttributes;
import ru.otus.homework.shell.event.ReadLanguageEvent;
import ru.otus.homework.shell.event.TestingStarterEvent;

@Service
public class EventsPublisherImpl implements EventsPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public EventsPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void testingStart(ShellAttributes shellAttributes) {
        applicationEventPublisher.publishEvent(new TestingStarterEvent(shellAttributes));
    }

    @Override
    public void readLanguage(ShellAttributes shellAttributes) {
        applicationEventPublisher.publishEvent(new ReadLanguageEvent(shellAttributes));
    }
}
