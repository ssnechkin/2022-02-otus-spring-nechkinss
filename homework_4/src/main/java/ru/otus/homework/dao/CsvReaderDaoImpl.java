package ru.otus.homework.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.otus.homework.config.ApplicationConfig;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CsvReaderDaoImpl implements CsvReaderDao {
    private final ApplicationConfig applicationConfig;

    public CsvReaderDaoImpl(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Override
    public List<String[]> getLineList(Locale locale) {
        List<String[]> lineList = new ArrayList<>();
        String path = applicationConfig.getCsvFileLocalePath().get(locale.toLanguageTag());
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