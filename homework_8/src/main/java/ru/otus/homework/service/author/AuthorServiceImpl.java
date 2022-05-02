package ru.otus.homework.service.author;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.book.Book;
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
    public Author getById(String id) {
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

    @Transactional
    @Override
    public void delete(Author author) {
        for (Book book : bookRepository.findAll()) {
            if (book.getAuthorIdList().contains(author.getId())) {
                book.getAuthorIdList().remove(author.getId());
                bookRepository.save(book);
            }
        }
        authorRepository.delete(author);
    }
}