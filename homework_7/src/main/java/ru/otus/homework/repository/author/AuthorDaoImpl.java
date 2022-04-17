package ru.otus.homework.repository.author;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.entity.Author;

import java.util.List;

@org.springframework.stereotype.Repository
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

    private final AuthorRepository authorRepository;

    @Override
    public void delete(long id) {
        authorRepository.findById(id).ifPresent(authorRepository::delete);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author getById(long id) {
        return authorRepository.getById(id);
    }

    @Override
    public Author insert(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public void update(Author author) {
        authorRepository.findById(author.getId()).ifPresent(authorRepository::save);
    }
}
