package ru.otus.homework.service.texter.author;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;

@Service
public class AuthorTexterImpl implements AuthorTexter {

    @Override
    public String add(Author author) {
        return "Author added. ID: " + author.getId();
    }

    @Override
    public String total(long count) {
        return "Total authors: " + count;
    }

    @Override
    public String toString(Author author) {
        return "Author"
                + " ID: " + author.getId()
                + " Surname: " + author.getSurname()
                + " Name: " + author.getName()
                + " Patronymic: " + author.getPatronymic();
    }

    @Override
    public String edit(Author author) {
        return "Edit Author"
                + " ID: " + author.getId()
                + " Surname: " + author.getSurname()
                + " Name: " + author.getName()
                + " Patronymic: " + author.getPatronymic();
    }

    @Override
    public String notFound(String id) {
        return "The author was not found by ID: " + id;
    }

    @Override
    public String delete(String id) {
        return "Author deleted. ID: " + id;
    }
}
