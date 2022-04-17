package ru.otus.homework.repository.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.BookComment;
import ru.otus.homework.repository.author.AuthorDaoImpl;
import ru.otus.homework.repository.book.comment.BookCommentDaoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookDaoImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class BookDaoImplTest {

    @Autowired
    private BookDaoImpl bookDao;

    @Autowired
    private AuthorDaoImpl authorDao;

    @Autowired
    private BookCommentDaoImpl bookCommentDao;

    @DisplayName("Добавление")
    @Test
    void insert() {
        Book book = new Book();
        book.setName("NameY");
        long id = bookDao.insert(book).getId();
        Book book1 = bookDao.getById(id);
        assertEquals(book1.getName(), "NameY");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Book book = new Book();
        book.setName("NameV");
        long id = bookDao.insert(book).getId();
        book.setName("NameF");
        bookDao.update(book);
        Book book1 = bookDao.getById(id);
        assertEquals("NameF", book1.getName());
    }

    @DisplayName("Добавить комментарий")
    @Test
    void addComment() {
        Book book = new Book();
        book.setName("NameV");
        long id = bookDao.insert(book).getId();

        ArrayList<BookComment> bookComments = new ArrayList<>();
        BookComment bookComment = new BookComment();
        String uuid = UUID.randomUUID().toString();
        bookComment.setComment(uuid);
        bookCommentDao.insert(bookComment);
        bookComments.add(bookComment);
        book.setComments(bookComments);
        bookDao.update(book);

        Book book1 = bookDao.getById(id);
        BookComment resultBookComment = null;
        for (BookComment bookComment1 : book1.getComments()) {
            if (bookComment1.getComment() != null && bookComment1.getComment().equals(uuid)) {
                resultBookComment = bookComment1;
                break;
            }
        }
        assert resultBookComment != null;
        assertEquals(resultBookComment.getComment(), uuid);
    }

    @DisplayName("Обновление комментария")
    @Test
    void updateComment() {
        String comment1 = UUID.randomUUID().toString();
        String comment2 = UUID.randomUUID().toString();
        Book book = new Book();
        book.setName("NameV");
        long id = bookDao.insert(book).getId();
        ArrayList<BookComment> bookComments = new ArrayList<>();
        BookComment bookComment = new BookComment();
        bookComment.setComment(comment1);
        bookCommentDao.insert(bookComment);
        bookComments.add(bookComment);
        book.setComments(bookComments);
        bookDao.update(book);
        Book book1 = bookDao.getById(id);
        for (BookComment bookComment2 : book1.getComments()) {
            if (bookComment2.getComment() != null && bookComment2.getComment().equals(comment1)) {
                bookComment2.setComment(comment2);
                bookCommentDao.update(bookComment2);
                break;
            }
        }
        Book book2 = bookDao.getById(id);
        BookComment bookCommentResult = null;
        for (BookComment bookComment2 : book2.getComments()) {
            if (bookComment2.getComment() != null && bookComment2.getComment().equals(comment2)) {
                bookCommentResult = bookComment2;
                break;
            }
        }

        assert bookCommentResult != null;
    }

    @DisplayName("Обновление наименование")
    @Test
    void updateName() {
        String name1 = UUID.randomUUID().toString();
        String name2 = UUID.randomUUID().toString();
        Book book = new Book();
        book.setName(name1);
        long id = bookDao.insert(book).getId();
        book.setName(name2);
        bookDao.update(book);
        Book book2 = bookDao.getById(id);
        assertEquals(book2.getName(), name2);
    }

    @DisplayName("Удаление комментария")
    @Test
    void deleteComment() {
        Book book = new Book();
        book.setName("NameV");
        bookDao.insert(book);

        ArrayList<BookComment> bookComments = new ArrayList<>();
        BookComment bookComment = new BookComment();
        String uuid = UUID.randomUUID().toString();
        bookComment.setComment(uuid);
        bookComments.add(bookComment);
        book.setComments(bookComments);
        bookDao.update(book);

        book.getComments().remove(bookComment);
        bookDao.update(book);
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        Book book1 = new Book();
        book1.setName("Name_delete1");
        bookDao.insert(book1);
        Book book2 = new Book();
        book2.setName("Name_delete2");
        long id = bookDao.insert(book2).getId();
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
        long id = bookDao.insert(book).getId();
        Book book1 = bookDao.getById(id);
        assertEquals("NameM", book1.getName());
    }

    @DisplayName("Удалить автора из книги")
    @Test
    void removeAuthorFormBook() {
        Book book = new Book();
        book.setName("NameVVD");
        book.setAuthors(new ArrayList<>());
        long id = bookDao.insert(book).getId();

        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        long aId = authorDao.insert(author).getId();

        Author author1 = authorDao.getById(aId);

        Book book1 = bookDao.getById(id);
        book1.getAuthors().add(author1);
        bookDao.update(book1);

        Book book2 = bookDao.getById(id);
        assertEquals(1, book2.getAuthors().size());
        book.getAuthors().remove(author);

        Book book3 = bookDao.getById(id);
        assertEquals(0, book3.getAuthors().size());
    }
}