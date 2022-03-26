package ru.otus.homework.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.otus.homework.config.ApplicationConfig;
import ru.otus.homework.service.provider.LocaleProvider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CsvReaderDaoImpl implements CsvReaderDao {
    private final ApplicationConfig applicationConfig;
    private final LocaleProvider localeProvider;

    public CsvReaderDaoImpl(LocaleProvider localeProvider, ApplicationConfig applicationConfig) {
        this.localeProvider = localeProvider;
        this.applicationConfig = applicationConfig;
    }

    @Override
    public List<String[]> getLineList() {
        List<String[]> lineList = new ArrayList<>();
        String path = applicationConfig.getCsvFileLocalePath().get(localeProvider.getLocale().toLanguageTag());
        try (InputStreamReader inputStreamReader =
                     new InputStreamReader(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8);
             CSVReader reader = new CSVReader(inputStreamReader)) {
            String[] record;
            while ((record = reader.readNext()) != null) {
                lineList.add(record);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return lineList;
    }
}