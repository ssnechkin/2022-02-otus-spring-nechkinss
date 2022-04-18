package ru.otus.homework.service.author;

import org.springframework.stereotype.Service;
import ru.otus.homework.entity.Author;
import ru.otus.homework.service.io.IOServiceStreams;
import ru.otus.homework.service.ext.Performance;

@Service
public class AuthorPerformance implements Performance<Author> {
    private final IOServiceStreams ioService;

    public AuthorPerformance(IOServiceStreams ioService) {
        this.ioService = ioService;
    }

    @Override
    public void delete(String id) {
        ioService.outputString("Author deleted. ID: " + id);
    }

    @Override
    public void add(String id) {
        ioService.outputString("Author added. ID: " + id);
    }

    @Override
    public void total(long count) {
        ioService.outputString("Total authors: " + count);
    }

    @Override
    public void notFound(String id) {
        ioService.outputString("The author was not found by ID: " + id);
    }

    @Override
    public void output(Author author) {
        ioService.outputString("Author"
                + " ID: " + author.getId()
                + " Surname: " + author.getSurname()
                + " Name: " + author.getName()
                + " Patronymic: " + author.getPatronymic()
        );
    }

}
