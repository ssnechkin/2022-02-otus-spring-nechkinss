package ru.otus.homework.dao.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookDaoImpl")
@JdbcTest
@Import(BookDaoImpl.class)
class BookDaoImplTest {
    @Autowired
    private BookDaoImpl bookDao;

    @DisplayName("Получить идентификатор")
    @Test
    void generateId() {
        assertTrue(bookDao.generateId() >= 1);
    }

    @DisplayName("Добавление")
    @Test
    void insert() {
        long id = bookDao.generateId();
        Book book = new Book(id, "NameY");
        bookDao.insert(book);
        List<Book> book1 = bookDao.getById(id);
        assertEquals(book1.get(0).getName(), "NameY");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        long id = bookDao.generateId();
        Book book = new Book(id, "NameV");
        bookDao.insert(book);
        book.setName("NameF");
        bookDao.update(book);
        List<Book> book1 = bookDao.getById(id);
        assertEquals("NameF", book1.get(0).getName());
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        long id = bookDao.generateId();
        Book book1 = new Book(id, "Name_delete1");
        bookDao.insert(book1);

        id = bookDao.generateId();
        Book book2 = new Book(id, "Name_delete2");
        bookDao.insert(book2);

        bookDao.delete(id);

        boolean name1Del = true;
        boolean name2Del = true;
        for (Book book : bookDao.getAll()) {
            if (book.getName().equals("Name_delete1")) {
                name1Del = false;
            }
            if (book.getName().equals("Name_delete2")) {
                name2Del = false;
            }
        }
        assertFalse(name1Del);
        assertTrue(name2Del);
    }

    @DisplayName("Получение всех")
    @Test
    void getAll() {
        long id = bookDao.generateId();
        Book book = new Book(id, "NameZ");
        bookDao.insert(book);
        List<Book> books = bookDao.getAll();
        assertTrue(books.size() >= 1);
    }

    @DisplayName("Получение одного")
    @Test
    void getById() {
        long id = bookDao.generateId();
        Book book = new Book(id, "NameM");
        bookDao.insert(book);
        List<Book> book1 = bookDao.getById(id);
        assertTrue(book1 != null && book1.get(0).getName().equals("NameM"));
    }

    @DisplayName("Количество")
    @Test
    void count() {
        Book book = new Book(bookDao.generateId(), "NameC");
        bookDao.insert(book);
        assertTrue(bookDao.count() >= 1);
    }
}