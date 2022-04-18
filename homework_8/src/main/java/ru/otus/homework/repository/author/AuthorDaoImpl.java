package ru.otus.homework.repository.author;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.entity.Author;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

    private final AuthorRepository repository;

    @Override
    public void delete(String id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public List<Author> getAll() {
        return repository.findAll();
    }

    @Override
    public Author getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Author insert(Author author) {
        return repository.save(author);
    }

    @Override
    public void update(Author author) {
        if (repository.findById(author.getId()).isPresent()) {
            repository.save(author);
        }
    }
}
