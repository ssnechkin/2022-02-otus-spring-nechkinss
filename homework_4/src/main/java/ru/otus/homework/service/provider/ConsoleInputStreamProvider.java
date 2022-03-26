package ru.otus.homework.service.provider;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ConsoleInputStreamProvider implements InputStreamProvider {
    private final InputStream inputStream = System.in;

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }
}
