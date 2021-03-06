package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.repository.author.AuthorDao;
import ru.otus.homework.repository.book.BookDao;
import ru.otus.homework.service.performance.Performance;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;
    private final BookDao bookDao;
    private final Performance<Author> performance;

    public AuthorServiceImpl(AuthorDao authorDao, BookDao bookDao, Performance<Author> performance) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
        this.performance = performance;
    }

    @Transactional
    @Override
    public void delete(long authorId) {
        Author author = authorDao.getById(authorId);
        if (author != null) {
            if (author.getBooks() != null) {
                for (Book book : author.getBooks()) {
                    book.getAuthors().remove(author);
                    bookDao.update(book);
                }
            }
            authorDao.delete(authorId);
            performance.delete(authorId);
        } else {
            performance.notFound(authorId);
        }
    }

    @Transactional
    @Override
    public void add(String surname, String name, String patronymic) {
        Author author = new Author();
        author.setSurname(surname);
        author.setName(name);
        author.setPatronymic(patronymic);
        performance.add(authorDao.insert(author));
    }

    @Transactional
    @Override
    public void outputAll() {
        List<Author> authors = authorDao.getAll();
        performance.total(authors.size());
        for (Author author : authors) {
            performance.output(author);
        }
    }
}
