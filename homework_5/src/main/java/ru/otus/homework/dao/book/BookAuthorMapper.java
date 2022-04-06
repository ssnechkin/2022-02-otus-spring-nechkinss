package ru.otus.homework.dao.book;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.domain.BookAuthor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookAuthorMapper implements RowMapper<BookAuthor> {
    @Override
    public BookAuthor mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BookAuthor(rs.getLong("book_id"), rs.getLong("author_id"));
    }
}
