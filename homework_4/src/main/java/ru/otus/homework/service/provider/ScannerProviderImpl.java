package ru.otus.homework.service.provider;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class ScannerProviderImpl implements ScannerProvider {
    private final Scanner scanner;

    public ScannerProviderImpl(InputStreamProvider inputStreamProvider) {
        scanner = new Scanner(inputStreamProvider.getInputStream(), StandardCharsets.UTF_8);
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }
}