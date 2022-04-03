package ru.otus.homework.dao.book;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Book(rs.getLong("id"), rs.getString("name"));
    }
}
