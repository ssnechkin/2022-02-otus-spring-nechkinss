package ru.otus.homework.service.provider;

import org.springframework.stereotype.Service;

import java.io.PrintStream;

@Service
public class ConsolePrintStreamProvider implements PrintStreamProvider {
    private final PrintStream printStream = System.out;

    @Override
    public void print(String prompt) {
        printStream.print(prompt);
    }

    @Override
    public void println(String prompt) {
        printStream.println(prompt);
    }
}
