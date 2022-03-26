package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.service.provider.PrintStreamProvider;
import ru.otus.homework.service.provider.ScannerProvider;

@Service
public class IOServiceStreams implements IOService {
    private final PrintStreamProvider printStreamProvider;
    private final ScannerProvider scannerProvider;

    public IOServiceStreams(ScannerProvider scannerProvider, PrintStreamProvider printStreamProvider) {
        this.scannerProvider = scannerProvider;
        this.printStreamProvider = printStreamProvider;
    }

    @Override
    public void outputString(String s) {
        printStreamProvider.println(s);
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        if (prompt != null) {
            printStreamProvider.print(prompt);
        }
        return scannerProvider.nextLine();
    }
}
