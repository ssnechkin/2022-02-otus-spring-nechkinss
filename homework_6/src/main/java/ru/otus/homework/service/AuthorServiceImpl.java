package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.service.io.IOServiceStreams;
import ru.otus.homework.repository.author.AuthorDao;
import ru.otus.homework.entity.Author;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;
    private final IOServiceStreams ioService;

    public AuthorServiceImpl(AuthorDao authorDao, IOServiceStreams ioService) {
        this.authorDao = authorDao;
        this.ioService = ioService;
    }

    @Override
    @Transactional
    public void delete(long authorId) {
        if (getAuthorOrOutPutNotFound(authorId) != null) {
            authorDao.delete(authorId);
            ioService.outputString("Author deleted. ID: " + authorId);
        }
    }

    @Override
    @Transactional
    public void add(String surname, String name, String patronymic) {
        long id = authorDao.insert(new Author(null, surname, name, patronymic));
        ioService.outputString("Author added. ID: " + id);
    }

    @Override
    @Transactional
    public void outputAll() {
        List<Author> authors = authorDao.getAll();
        ioService.outputString("Total authors: " + authorDao.count());
        for (Author author : authors) {
            outputAuthor(author);
        }
    }

    private Author getAuthorOrOutPutNotFound(long authorId) {
        List<Author> authors = authorDao.getById(authorId);
        if (authors == null) {
            ioService.outputString("The author was not found by ID: " + authorId);
            return null;
        }
        return authors.get(0);
    }

    private void outputAuthor(Author author) {
        ioService.outputString("Author"
                + " ID: " + author.getId()
                + " Surname: " + author.getSurname()
                + " Name: " + author.getName()
                + " Patronymic: " + author.getPatronymic()
        );
    }
}
