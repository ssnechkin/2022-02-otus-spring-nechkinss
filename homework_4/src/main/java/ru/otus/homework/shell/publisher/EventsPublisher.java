package ru.otus.homework.shell.publisher;

import ru.otus.homework.domain.ShellAttributes;

public interface EventsPublisher {
    void testingStart(ShellAttributes shellAttributes);

    void readLanguage(ShellAttributes shellAttributes);
}
