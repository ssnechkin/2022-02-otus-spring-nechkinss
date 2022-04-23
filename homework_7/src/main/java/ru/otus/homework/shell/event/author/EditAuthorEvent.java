package ru.otus.homework.shell.event.author;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class EditAuthorEvent extends ApplicationEvent {

    @Getter
    private final long authorId;

    @Getter
    private final String surname, name, patronymic;

    public EditAuthorEvent(long authorId, String surname, String name, String patronymic) {
        super(Map.of("authorId", authorId, "surname", surname, "name", name, "patronymic", patronymic));
        this.authorId = authorId;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
    }
}
