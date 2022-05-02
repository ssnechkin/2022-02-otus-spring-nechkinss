package ru.otus.homework.service.texter.genre;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Genre;

@Service
public class GenreTexterImpl implements GenreTexter {

    @Override
    public String add(Genre genre) {
        return "Genre added. ID: " + genre.getId();
    }

    @Override
    public String total(long count) {
        return "Total genre: " + count;
    }

    @Override
    public String toString(Genre genre) {
        return "Genre"
                + " ID: " + genre.getId()
                + " Name: " + genre.getName()
                + " Description: " + genre.getDescription();
    }

    @Override
    public String edit(Genre genre) {
        return "Edit Genre"
                + " ID: " + genre.getId()
                + " Name: " + genre.getName()
                + " Description: " + genre.getDescription();
    }

    @Override
    public String notFound(String id) {
        return "The genre was not found by ID: " + id;
    }

    @Override
    public String delete(String id) {
        return "Genre deleted. ID: " + id;
    }
}
