package ru.otus.homework.dao.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.BookAuthor;
import ru.otus.homework.domain.BookGenre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookAuthorDaoImpl")
@JdbcTest
@Import(BookAuthorDaoImpl.class)
class BookAuthorDaoImplTest {
    @Autowired
    private BookAuthorDaoImpl bookAuthorDao;

    @DisplayName("Добавление")
    @Test
    void insert() {
        BookAuthor bookAuthor = new BookAuthor(1, 2);
        bookAuthorDao.insert(bookAuthor);
        List<BookAuthor> bookAuthors = bookAuthorDao.getListById(1);
        assertTrue(bookAuthors.size() >= 1);
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        BookAuthor bookAuthor1 = new BookAuthor(3, 4);
        bookAuthorDao.insert(bookAuthor1);

        BookAuthor bookAuthor2 = new BookAuthor(3, 5);
        bookAuthorDao.insert(bookAuthor2);

        bookAuthorDao.delete(3, 5);

        boolean name1Del = true;
        boolean name2Del = true;
        for (BookAuthor bookAuthor : bookAuthorDao.getListById(3)) {
            if (bookAuthor.getAuthorsId() == 4) {
                name1Del = false;
            }
            if (bookAuthor.getAuthorsId() == 5) {
                name2Del = false;
            }
        }
        assertFalse(name1Del);
        assertTrue(name2Del);
    }

    @DisplayName("Получение всех по id")
    @Test
    void getAll() {
        BookAuthor bookAuthor1 = new BookAuthor(6, 7);
        BookAuthor bookAuthor2 = new BookAuthor(6, 8);
        bookAuthorDao.insert(bookAuthor1);
        bookAuthorDao.insert(bookAuthor2);
        List<BookAuthor> bookAuthors = bookAuthorDao.getListById(6);
        assertEquals(2, bookAuthors.size());
    }

    @DisplayName("Существования одного")
    @Test
    void getById() {
        BookAuthor bookAuthor1 = new BookAuthor(9, 10);
        BookAuthor bookAuthor2 = new BookAuthor(9, 11);
        bookAuthorDao.insert(bookAuthor1);
        bookAuthorDao.insert(bookAuthor2);
        assertTrue(bookAuthorDao.isExist(9, 11));
        assertFalse(bookAuthorDao.isExist(9, 12));
    }


    @DisplayName("Удаление ссылок")
    @Test
    void deleteLinks() {
        BookAuthor bookAuthor1 = new BookAuthor(7, 10);
        BookAuthor bookAuthor2 = new BookAuthor(10, 8);
        BookAuthor bookAuthor3 = new BookAuthor(9, 10);
        bookAuthorDao.insert(bookAuthor1);
        bookAuthorDao.insert(bookAuthor2);
        bookAuthorDao.insert(bookAuthor3);
        bookAuthorDao.deleteLinks(10);
        assertTrue(bookAuthorDao.isExist(10, 8));
        assertFalse(bookAuthorDao.isExist(7, 10));
        assertFalse(bookAuthorDao.isExist(9, 10));
    }
}