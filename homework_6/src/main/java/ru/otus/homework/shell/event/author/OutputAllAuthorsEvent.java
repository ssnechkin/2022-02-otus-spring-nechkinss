package ru.otus.homework.shell.event.author;

import org.springframework.context.ApplicationEvent;

public class OutputAllAuthorsEvent extends ApplicationEvent {

    public OutputAllAuthorsEvent() {
        super("");
    }
}
