package ru.otus.homework.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReaderDaoImpl implements CsvReaderDao {
    private final ClassPathResource classPathResource;
    private final List<String[]> lineList = new ArrayList<>();

    public CsvReaderDaoImpl(@Value("${csv-file}") String path) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        this.classPathResource = classPathResource;
    }

    private void read() throws IOException, CsvValidationException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
             CSVReader reader = new CSVReader(inputStreamReader)) {
            String[] record;
            while ((record = reader.readNext()) != null) {
                lineList.add(record);
            }
        }
    }

    @Override
    public List<String[]> getLineList() throws CsvValidationException, IOException {
        read();
        return this.lineList;
    }
}