package ru.otus.homework.dao.book;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.BookAuthor;

import java.util.List;
import java.util.Map;

@Repository
public class BookAuthorDaoImpl implements BookAuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    public BookAuthorDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean isExist(long bookId, long authorId) {
        Long count = jdbc.queryForObject("SELECT count(1) FROM books_authors WHERE book_id = :book_id AND author_id = :author_id",
                Map.of("book_id", bookId, "author_id", authorId), Long.class);
        return count != null && count > 0;
    }

    @Override
    public void delete(long bookId, long authorId) {
        jdbc.update("DELETE books_authors WHERE book_id = :book_id AND author_id = :author_id",
                Map.of("book_id", bookId, "author_id", authorId));
    }

    @Override
    public void deleteLinks(long authorId) {
        jdbc.update("DELETE books_authors WHERE author_id = :author_id",
                Map.of("author_id", authorId));
    }

    @Override
    public void insert(BookAuthor object) {
        jdbc.update("INSERT INTO books_authors (book_id, author_id) VALUES (:book_id, :author_id)",
                Map.of("book_id", object.getBooksId(), "author_id", object.getAuthorsId()));
    }

    @Override
    public List<BookAuthor> getListById(long id) {
        return jdbc.query("SELECT book_id, author_id FROM books_authors WHERE book_id = :book_id",
                Map.of("book_id", id), new BookAuthorMapper());
    }
}
