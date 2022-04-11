package ru.otus.homework.repository.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.entity.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookDaoImpl")
@DataJpaTest
@Import(BookDaoImpl.class)
class BookDaoImplTest {
    @Autowired
    private BookDaoImpl bookDao;

    @DisplayName("Добавление")
    @Test
    void insert() {
        Book book = new Book();
        book.setName("NameY");
        long id = bookDao.insert(book);
        List<Book> book1 = bookDao.getById(id);
        assertEquals(book1.get(0).getName(), "NameY");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Book book = new Book();
        book.setName("NameV");
        long id = bookDao.insert(book);
        book.setName("NameF");
        bookDao.update(book);
        List<Book> book1 = bookDao.getById(id);
        assertEquals("NameF", book1.get(0).getName());
    }

    @DisplayName("Обновление комментария")
    @Test
    void updateComment() {
        Book book = new Book();
        book.setName("NameV");
        book.setComment("CommentV");
        long id = bookDao.insert(book);
        book.setName("NameF");
        book.setComment("CommentF");
        bookDao.update(book);
        List<Book> book1 = bookDao.getById(id);
        assertEquals("NameF", book1.get(0).getName());
        assertEquals("CommentF", book1.get(0).getComment());
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        Book book1 = new Book();
        book1.setName("Name_delete1");
        bookDao.insert(book1);
        Book book2 = new Book();
        book2.setName("Name_delete2");
        long id = bookDao.insert(book2);
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
        Book book = new Book();
        book.setName("NameZ");
        bookDao.insert(book);
        List<Book> books = bookDao.getAll();
        assertTrue(books.size() >= 1);
    }

    @DisplayName("Получение одного")
    @Test
    void getById() {
        Book book = new Book();
        book.setName("NameM");
        long id = bookDao.insert(book);
        List<Book> book1 = bookDao.getById(id);
        assertTrue(book1 != null && book1.get(0).getName().equals("NameM"));
    }

    @DisplayName("Количество")
    @Test
    void count() {
        Book book = new Book();
        book.setName("NameC");
        bookDao.insert(book);
        assertTrue(bookDao.count() >= 1);
    }
}