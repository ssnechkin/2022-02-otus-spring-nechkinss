package ru.otus.homework.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import ru.otus.homework.service.CsvLineService;
import ru.otus.homework.service.mapping.CsvLineToObjectService;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Выполняет чтение csv-файла и запускает процесс обработки каждой строки
 *
 * @author NechkinSS
 */
public class CsvResourceReaderDao implements ResourceReaderDao {

    public CsvResourceReaderDao(ClassPathResource classPathResource, CsvLineToObjectService csvLineToObjectService, CsvLineService csvLineService) throws IOException, CsvValidationException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
             CSVReader reader = new CSVReader(inputStreamReader)) {
            read(reader, csvLineToObjectService, csvLineService);
        }
    }

    /**
     * Выполняет чтение csv-файла и запускает процесс обработки каждой строки
     *
     * @param reader                 Читатель csv-файла
     * @param csvLineToObjectService Конвертер строки csv-файла в объект
     * @param csvLineService         Класс для обработки строки csv-файла
     * @throws CsvValidationException Файл не соответствует csv-формату
     * @throws IOException            Ошибка чтения файла
     */
    @Override
    public void read(CSVReader reader, CsvLineToObjectService csvLineToObjectService, CsvLineService csvLineService) throws CsvValidationException, IOException {
        String[] record;

        while ((record = reader.readNext()) != null) {
            csvLineService.processAline(csvLineToObjectService.convert(record));
        }
    }
}
