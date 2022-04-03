package ru.otus.homework.dao.book;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.BookGenre;

import java.util.List;
import java.util.Map;

@Repository
public class BookGenreDaoImpl implements BookGenreDao {
    private final NamedParameterJdbcOperations jdbc;

    public BookGenreDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean isExist(long bookId, long genreId) {
        Long count = jdbc.queryForObject("SELECT count(1) FROM books_genres WHERE book_id = :book_id AND genre_id = :genre_id",
                Map.of("book_id", bookId, "genre_id", genreId), Long.class);
        return count != null && count > 0;
    }

    @Override
    public void delete(long bookId, long genreId) {
        jdbc.update("DELETE books_genres WHERE book_id = :book_id AND genre_id = :genre_id",
                Map.of("book_id", bookId, "genre_id", genreId));
    }

    @Override
    public void deleteLinks(long genreId) {
        jdbc.update("DELETE books_genres WHERE genre_id = :genre_id",
                Map.of("genre_id", genreId));
    }

    @Override
    public void insert(BookGenre object) {
        jdbc.update("INSERT INTO books_genres (book_id, genre_id) VALUES (:book_id, :genre_id)",
                Map.of("book_id", object.getBooksId(), "genre_id", object.getGenresId()));
    }

    @Override
    public List<BookGenre> getListById(long id) {
        return jdbc.query("SELECT book_id, genre_id FROM books_genres WHERE book_id = :book_id",
                Map.of("book_id", id), new BookGenreMapper());
    }
}
