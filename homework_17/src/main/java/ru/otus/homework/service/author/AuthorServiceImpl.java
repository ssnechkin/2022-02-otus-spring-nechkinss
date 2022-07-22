package ru.otus.homework.service.author;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.entity.Menu;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.repository.MenuRepository;
import ru.otus.homework.repository.author.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
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