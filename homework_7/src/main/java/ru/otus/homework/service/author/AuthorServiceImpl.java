package ru.otus.homework.service.author;

import org.springframework.stereotype.Service;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.repository.author.AuthorRepository;
import ru.otus.homework.repository.book.BookRepository;
import ru.otus.homework.service.performance.Performance;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final Performance<Author> performance;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository, Performance<Author> performance) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.performance = performance;
    }

    @Override
    public void delete(long authorId) {
        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent()) {
            if (author.get().getBooks() != null) {
                for (Book book : bookRepository.findAll()) {
                    if (book.getAuthors().contains(author.get())) {
                        book.getAuthors().remove(author.get());
                        bookRepository.save(book);
                    }
                }
            }
            authorRepository.delete(author.get());
            performance.delete(authorId);
        } else {
            performance.notFound(authorId);
        }
    }

    @Override
    public void add(String surname, String name, String patronymic) {
        Author author = new Author();
        author.setSurname(surname);
        author.setName(name);
        author.setPatronymic(patronymic);
        performance.add(authorRepository.save(author).getId());
    }

    @Override
    public void outputAll() {
        List<Author> authors = authorRepository.findAll();
        performance.total(authors.size());
        for (Author author : authors) {
            performance.output(author);
        }
    }

    @Override
    public void edit(long authorId, String surname, String name, String patronymic) {
        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent()) {
            Author newAuthor = author.get();
            newAuthor.setSurname(surname);
            newAuthor.setName(name);
            newAuthor.setPatronymic(patronymic);
            authorRepository.save(newAuthor);
            performance.edit(authorId, newAuthor);
        } else {
            performance.notFound(authorId);
        }
    }
}
