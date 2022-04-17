package ru.otus.homework.shell.listener;

import org.springframework.stereotype.Component;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.shell.event.author.AddAuthorEvent;
import ru.otus.homework.shell.event.author.DeleteAuthorByIdEvent;
import ru.otus.homework.shell.event.author.OutputAllAuthorsEvent;

@Component
public class AuthorShellEventListener {
    private final AuthorService authorService;

    public AuthorShellEventListener(AuthorService authorService) {
        this.authorService = authorService;
    }

    @org.springframework.context.event.EventListener
    public void addAuthorEvent(AddAuthorEvent event) {
        authorService.add(event.getSurname(), event.getName(), event.getPatronymic());
    }

    @org.springframework.context.event.EventListener
    public void deleteAuthorByIdEvent(DeleteAuthorByIdEvent event) {
        authorService.delete(event.getAuthorId());
    }

    @org.springframework.context.event.EventListener
    public void outputAllAuthorsEvent(OutputAllAuthorsEvent event) {
        authorService.outputAll();
    }
}