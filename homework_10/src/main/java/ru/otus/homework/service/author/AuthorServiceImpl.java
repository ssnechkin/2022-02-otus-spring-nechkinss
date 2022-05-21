package ru.otus.homework.service.author;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.repository.author.AuthorRepository;
import ru.otus.homework.repository.book.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Author add(String surname, String name, String patronymic) {
        Author author = new Author();
        author.setSurname(surname);
        author.setName(name);
        author.setPatronymic(patronymic);
        return authorRepository.save(author);
    }

    @Override
    public Author getById(long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.orElse(null);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author edit(Author author, String surname, String name, String patronymic) {
        author.setSurname(surname);
        author.setName(name);
        author.setPatronymic(patronymic);
        return authorRepository.save(author);
    }

    @Override
    public boolean delete(Author author) {
        if (author.getBooks().size() > 0) {
            return false;
        } else {
            authorRepository.delete(author);
            return true;
        }
    }
}