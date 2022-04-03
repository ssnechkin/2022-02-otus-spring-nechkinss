package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.config.IOServiceStreams;
import ru.otus.homework.dao.author.AuthorDao;
import ru.otus.homework.dao.book.BookAuthorDao;
import ru.otus.homework.domain.Author;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;
    private final BookAuthorDao bookAuthorDao;
    private final IOServiceStreams ioService;

    public AuthorServiceImpl(AuthorDao authorDao, BookAuthorDao bookAuthorDao, IOServiceStreams ioService) {
        this.authorDao = authorDao;
        this.bookAuthorDao = bookAuthorDao;
        this.ioService = ioService;
    }

    @Override
    public void delete(long authorId) {
        if (getAuthorOrOutPutNotFound(authorId) != null) {
            authorDao.delete(authorId);
            bookAuthorDao.deleteLinks(authorId);
            ioService.outputString("Author deleted. ID: " + authorId);
        }
    }

    @Override
    public void add(String surname, String name, String patronymic) {
        long id = authorDao.generateId();
        authorDao.insert(new Author(id, surname, name, patronymic));
        ioService.outputString("Author added. ID: " + id);
    }

    @Override
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
