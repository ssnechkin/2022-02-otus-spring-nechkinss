package ru.otus.homework.shell.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.otus.homework.domain.ShellAttributes;

public class TestingStarterEvent extends ApplicationEvent {
    @Getter
    private final ShellAttributes shellAttributes;

    public TestingStarterEvent(ShellAttributes shellAttributes) {
        super(shellAttributes);
        this.shellAttributes = shellAttributes;
    }
}
