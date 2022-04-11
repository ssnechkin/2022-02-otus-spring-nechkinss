package ru.otus.homework.shell.event.author;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class AddAuthorEvent extends ApplicationEvent {
    @Getter
    private final String surname, name, patronymic;

    public AddAuthorEvent(String surname, String name, String patronymic) {
        super(Map.of("surname", surname, "name", name, "patronymic", patronymic));
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
    }
}
