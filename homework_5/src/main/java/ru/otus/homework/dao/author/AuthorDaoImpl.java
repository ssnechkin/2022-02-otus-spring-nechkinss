package ru.otus.homework.dao.author;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Author;

import java.util.List;
import java.util.Map;

@Repository
public class AuthorDaoImpl implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations;
    }

    @Override
    public long count() {
        Long count = jdbc.queryForObject("SELECT count(1) FROM authors",
                Map.of(), Long.class);
        return count == null ? 0 : count;
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE authors WHERE id = :id",
                Map.of("id", id));
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("SELECT id, surname, name, patronymic FROM authors", new AuthorMapper());
    }

    @Override
    public List<Author> getById(long id) {
        return jdbc.query("SELECT id, surname, name, patronymic FROM authors WHERE id = :id",
                Map.of("id", id), new AuthorMapper());
    }

    @Override
    public void insert(Author object) {
        jdbc.update("INSERT INTO authors (id, surname, name, patronymic) VALUES (:id, :surname, :name, :patronymic)",
                Map.of("id", object.getId(),
                        "surname", object.getSurname(),
                        "name", object.getName(),
                        "patronymic", object.getPatronymic()));
    }

    @Override
    public void update(Author object) {
        jdbc.update("UPDATE authors set surname = :surname, name = :name, patronymic = :patronymic WHERE id = :id",
                Map.of("id", object.getId(),
                        "surname", object.getSurname(),
                        "name", object.getName(),
                        "patronymic", object.getPatronymic()));
    }

    @Override
    public long generateId() {
        Long id = jdbc.queryForObject("SELECT authors_sequence.nextval FROM DUAL",
                Map.of(), Long.class);
        return id != null ? id : 0;
    }
}
