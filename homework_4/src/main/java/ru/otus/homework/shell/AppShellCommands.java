package ru.otus.homework.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.domain.ShellAttributes;
import ru.otus.homework.shell.publisher.EventsPublisher;

@ShellComponent
public class AppShellCommands {
    private final EventsPublisher eventsPublisher;
    private final ShellAttributes shellAttributes;

    public AppShellCommands(EventsPublisher eventsPublisher) {
        this.eventsPublisher = eventsPublisher;
        this.shellAttributes = new ShellAttributes();
    }

    @ShellMethod(value = "Enter your last name", key = {"l", "last-name"})
    public void lastName(@ShellOption String lastName) {
        this.shellAttributes.setLastName(lastName);
    }

    @ShellMethod(value = "Enter your first name", key = {"f", "first-name"})
    public void firstName(@ShellOption String firstName) {
        this.shellAttributes.setFirstName(firstName);
    }

    @ShellMethod(value = "Enter your language", key = {"lang"})
    public void lang() {
        eventsPublisher.readLanguage(shellAttributes);
    }

    @ShellMethod(value = "Start testing", key = {"t", "start", "begin"})
    @ShellMethodAvailability(value = "isCorrectParams")
    public void startTesting() {
        eventsPublisher.testingStart(shellAttributes);
    }

    private Availability isCorrectParams() {
        Availability availability = isSpecifiedLastName();
        if (availability.isAvailable()) {
            availability = isSpecifiedFirstName();
        }
        return availability;
    }

    private Availability isSpecifiedLastName() {
        return shellAttributes.getLastName() == null || shellAttributes.getLastName().length() == 0
                ? Availability.unavailable("Your last name is not entered. --l")
                : Availability.available();
    }

    private Availability isSpecifiedFirstName() {
        return shellAttributes.getFirstName() == null || shellAttributes.getFirstName().length() == 0
                ? Availability.unavailable("Your first name is not entered. --f")
                : Availability.available();
    }
}
