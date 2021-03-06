package ru.otus.homework.service.performance.author;

import org.springframework.stereotype.Service;
import ru.otus.homework.entity.Author;
import ru.otus.homework.service.io.IOServiceStreams;
import ru.otus.homework.service.performance.Performance;

@Service
public class AuthorPerformance implements Performance<Author> {

    private final IOServiceStreams ioService;

    public AuthorPerformance(IOServiceStreams ioService) {
        this.ioService = ioService;
    }

    @Override
    public void delete(long id) {
        ioService.outputString("Author deleted. ID: " + id);
    }

    @Override
    public void add(long id) {
        ioService.outputString("Author added. ID: " + id);
    }

    @Override
    public void total(long count) {
        ioService.outputString("Total authors: " + count);
    }

    @Override
    public void notFound(long id) {
        ioService.outputString("The author was not found by ID: " + id);
    }

    @Override
    public void edit(long id, Author author) {
        ioService.outputString("Edit Author"
                + " ID: " + author.getId()
                + " Surname: " + author.getSurname()
                + " Name: " + author.getName()
                + " Patronymic: " + author.getPatronymic()
        );
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
