package ru.otus.homework.dao.book;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Map;

@Repository
public class BookDaoImpl implements BookDao {
    private final NamedParameterJdbcOperations jdbc;

    public BookDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations;
    }

    @Override
    public long count() {
        Long count = jdbc.queryForObject("SELECT count(1) FROM books",
                Map.of(), Long.class);
        return count == null ? 0 : count;
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE books WHERE id = :id",
                Map.of("id", id));
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("SELECT id, name FROM books",
                Map.of(), new BookMapper());
    }

    @Override
    public List<Book> getById(long id) {
        return jdbc.query("SELECT id, name FROM books WHERE id = :id",
                Map.of("id", id), new BookMapper());
    }

    @Override
    public void insert(Book object) {
        jdbc.update("INSERT INTO books (id, name) VALUES (:id, :name)",
                Map.of("id", object.getId(), "name", object.getName()));
    }

    @Override
    public void update(Book object) {
        jdbc.update("UPDATE books set name = :name WHERE id = :id",
                Map.of("id", object.getId(), "name", object.getName()));
    }

    @Override
    public long generateId() {
        Long id = jdbc.queryForObject("SELECT books_sequence.nextval FROM DUAL",
                Map.of(), Long.class);
        return id != null ? id : 0;
    }
}
