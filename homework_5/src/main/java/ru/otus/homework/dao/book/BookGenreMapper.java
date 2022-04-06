package ru.otus.homework.dao.book;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.homework.domain.BookGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookGenreMapper implements RowMapper<BookGenre> {
    @Override
    public BookGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BookGenre(rs.getLong("book_id"), rs.getLong("genre_id"));
    }
}
