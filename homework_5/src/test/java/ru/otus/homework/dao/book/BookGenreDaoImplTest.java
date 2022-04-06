package ru.otus.homework.dao.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.BookGenre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookGenreDaoImpl")
@JdbcTest
@Import(BookGenreDaoImpl.class)
class BookGenreDaoImplTest {
    @Autowired
    private BookGenreDaoImpl bookGenreDao;

    @DisplayName("Добавление")
    @Test
    void insert() {
        BookGenre bookGenre = new BookGenre(1, 2);
        bookGenreDao.insert(bookGenre);
        List<BookGenre> bookGenres = bookGenreDao.getListById(1);
        assertTrue(bookGenres.size() >= 1);
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        BookGenre bookGenre1 = new BookGenre(3, 4);
        bookGenreDao.insert(bookGenre1);

        BookGenre bookGenre2 = new BookGenre(3, 5);
        bookGenreDao.insert(bookGenre2);

        bookGenreDao.delete(3, 5);

        boolean name1Del = true;
        boolean name2Del = true;
        for (BookGenre bookGenre : bookGenreDao.getListById(3)) {
            if (bookGenre.getGenresId() == 4) {
                name1Del = false;
            }
            if (bookGenre.getGenresId() == 5) {
                name2Del = false;
            }
        }
        assertFalse(name1Del);
        assertTrue(name2Del);
    }

    @DisplayName("Получение всех по id")
    @Test
    void getAll() {
        BookGenre bookGenre1 = new BookGenre(6, 7);
        BookGenre bookGenre2 = new BookGenre(6, 8);
        bookGenreDao.insert(bookGenre1);
        bookGenreDao.insert(bookGenre2);
        List<BookGenre> bookGenres = bookGenreDao.getListById(6);
        assertEquals(2, bookGenres.size());
    }

    @DisplayName("Существования одного")
    @Test
    void getById() {
        BookGenre bookGenre1 = new BookGenre(9, 10);
        BookGenre bookGenre2 = new BookGenre(9, 11);
        bookGenreDao.insert(bookGenre1);
        bookGenreDao.insert(bookGenre2);
        assertTrue(bookGenreDao.isExist(9, 11));
        assertFalse(bookGenreDao.isExist(9, 12));
    }

    @DisplayName("Удаление ссылок")
    @Test
    void deleteLinks() {
        BookGenre bookGenre1 = new BookGenre(7, 10);
        BookGenre bookGenre2 = new BookGenre(10, 8);
        BookGenre bookGenre3 = new BookGenre(9, 10);
        bookGenreDao.insert(bookGenre1);
        bookGenreDao.insert(bookGenre2);
        bookGenreDao.insert(bookGenre3);
        bookGenreDao.deleteLinks(10);
        assertTrue(bookGenreDao.isExist(10, 8));
        assertFalse(bookGenreDao.isExist(7, 10));
        assertFalse(bookGenreDao.isExist(9, 10));
    }
}