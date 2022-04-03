package ru.otus.homework.dao.author;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        Author author = new Author();
        author.setId(rs.getLong("id"));
        author.setSurname(rs.getString("surname"));
        author.setName(rs.getString("name"));
        author.setPatronymic(rs.getString("patronymic"));
        return author;
    }
}
