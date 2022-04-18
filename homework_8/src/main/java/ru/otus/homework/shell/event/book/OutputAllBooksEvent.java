package ru.otus.homework.shell.event.book;

import org.springframework.context.ApplicationEvent;

public class OutputAllBooksEvent extends ApplicationEvent {

    public OutputAllBooksEvent() {
        super("");
    }
}
