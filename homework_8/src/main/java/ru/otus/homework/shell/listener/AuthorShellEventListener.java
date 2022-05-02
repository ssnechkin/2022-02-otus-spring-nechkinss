package ru.otus.homework.shell.listener;

import org.springframework.stereotype.Component;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.performance.author.AuthorPerformance;
import ru.otus.homework.shell.event.author.AddAuthorEvent;
import ru.otus.homework.shell.event.author.DeleteAuthorByIdEvent;
import ru.otus.homework.shell.event.author.EditAuthorEvent;
import ru.otus.homework.shell.event.author.OutputAllAuthorsEvent;

@Component
public class AuthorShellEventListener {

    private final AuthorPerformance authorPerformance;
    private final IOService ioService;

    public AuthorShellEventListener(AuthorPerformance authorPerformance, IOService ioService) {
        this.authorPerformance = authorPerformance;
        this.ioService = ioService;
    }

    @org.springframework.context.event.EventListener
    public void addAuthorEvent(AddAuthorEvent event) {
        ioService.outputString(authorPerformance.add(event.getSurname(), event.getName(), event.getPatronymic()));
    }

    @org.springframework.context.event.EventListener
    public void outputAllAuthorsEvent(OutputAllAuthorsEvent event) {
        for (String line : authorPerformance.getAll()) {
            ioService.outputString(line);
        }
    }

    @org.springframework.context.event.EventListener
    public void editAuthorEvent(EditAuthorEvent event) {
        ioService.outputString(authorPerformance.edit(event.getAuthorId(), event.getSurname(), event.getName(), event.getPatronymic()));
    }

    @org.springframework.context.event.EventListener
    public void deleteAuthorByIdEvent(DeleteAuthorByIdEvent event) {
        ioService.outputString(authorPerformance.deleteById(event.getAuthorId()));
    }
}