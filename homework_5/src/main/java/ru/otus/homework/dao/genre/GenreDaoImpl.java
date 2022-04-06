package ru.otus.homework.dao.genre;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Map;

@Repository
public class GenreDaoImpl implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations;
    }

    @Override
    public long count() {
        Long count = jdbc.queryForObject("SELECT count(1) FROM genres",
                Map.of(), Long.class);
        return count == null ? 0 : count;
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE genres WHERE id = :id",
                Map.of("id", id));
    }

    @Override
    public List<Genre> getAll() {
            return jdbc.query("SELECT id, name, description FROM genres",
                    Map.of(), new GenreMapper());
    }

    @Override
    public List<Genre> getById(long id) {
        return jdbc.query("SELECT id, name, description FROM genres WHERE id = :id",
                Map.of("id", id), new GenreMapper());
    }

    @Override
    public void insert(Genre object) {
        jdbc.update("INSERT INTO genres (id, name, description) VALUES (:id, :name, :description)",
                Map.of("id", object.getId(), "name", object.getName(), "description", object.getDescription()));
    }

    @Override
    public void update(Genre object) {
        jdbc.update("UPDATE genres set name = :name, description = :description WHERE id = :id",
                Map.of("id", object.getId(), "name", object.getName(), "description", object.getDescription()));
    }

    @Override
    public long generateId() {
        Long id = jdbc.queryForObject("SELECT genres_sequence.nextval FROM DUAL",
                Map.of(), Long.class);
        return id != null ? id : 0;
    }
}
