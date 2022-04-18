package ru.otus.homework.repository.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.BookComment;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.repository.author.AuthorDaoImpl;
import ru.otus.homework.repository.book.comment.BookCommentDaoImpl;
import ru.otus.homework.repository.genre.GenreDaoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookDaoImpl")
@DataMongoTest
@ComponentScan("ru.otus.homework")
class BookDaoImplTest {

    @Autowired
    private BookDaoImpl bookDao;

    @Autowired
    private AuthorDaoImpl authorDao;

    @Autowired
    private GenreDaoImpl genreDao;

    @Autowired
    private BookCommentDaoImpl bookCommentDao;

    @DisplayName("Добавление")
    @Test
    void insert() {
        Book book = new Book();
        book.setName("NameY");
        book = bookDao.insert(book);
        Book book1 = bookDao.getById(book.getId());
        assertEquals(book1.getName(), "NameY");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Book book = new Book();
        book.setName("NameV");
        book = bookDao.insert(book);
        book.setName("NameF");
        bookDao.update(book);
        Book book1 = bookDao.getById(book.getId());
        assertEquals("NameF", book1.getName());
    }

    @DisplayName("Добавить комментарий")
    @Test
    void addComment() {
        Book book = new Book();
        book.setName("NameV");
        book = bookDao.insert(book);

        ArrayList<BookComment> bookComments = new ArrayList<>();
        BookComment bookComment = new BookComment();
        String uuid = UUID.randomUUID().toString();
        bookComment.setComment(uuid);
        bookCommentDao.insert(bookComment);
        bookComments.add(bookComment);
        book.setComments(bookComments);
        bookDao.update(book);

        Book book1 = bookDao.getById(book.getId());
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
        book = bookDao.insert(book);

        BookComment bookComment = new BookComment();
        bookComment.setComment(comment1);

        book.getComments().add(bookComment);
        bookDao.update(book);

        assertEquals(1, book.getComments().size());

        bookComment = book.getComments().get(0);
        bookComment.setComment(comment2);
        bookDao.update(book);

        Book book2 = bookDao.getById(book.getId());
        assertEquals(comment2, book2.getComments().get(0).getComment());
    }

    @DisplayName("Обновление наименование")
    @Test
    void updateName() {
        String name1 = UUID.randomUUID().toString();
        String name2 = UUID.randomUUID().toString();
        Book book = new Book();
        book.setName(name1);
        book = bookDao.insert(book);
        book.setName(name2);
        bookDao.update(book);
        Book book2 = bookDao.getById(book.getId());
        assertEquals(book2.getName(), name2);
    }

    @DisplayName("Удаление комментария")
    @Test
    void deleteComment() {
        Book book = new Book();
        book.setName("NameV");
        bookDao.insert(book);

        BookComment bookComment = new BookComment();
        String uuid = UUID.randomUUID().toString();
        bookComment.setComment(uuid);
        book.getComments().add(bookComment);
        bookDao.update(book);

        assertEquals(1, book.getComments().size());

        book.getComments().remove(bookComment);
        bookDao.update(book);

        book = bookDao.getById(book.getId());

        assertEquals(0, book.getComments().size());
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        Book book1 = new Book();
        book1.setName("Name_delete1");
        bookDao.insert(book1);

        Book book2 = new Book();
        book2.setName("Name_delete2");
        book2 = bookDao.insert(book2);

        bookDao.delete(book2.getId());

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
        book = bookDao.insert(book);
        Book book1 = bookDao.getById(book.getId());
        assertEquals("NameM", book1.getName());
    }

    @DisplayName("Удалить автора из книги")
    @Test
    void removeAuthorFormBook() {
        Book book = new Book();
        book.setName("NameVVD");
        book.setAuthors(new ArrayList<>());
        book = bookDao.insert(book);

        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        author = authorDao.insert(author);

        book.getAuthors().add(author);
        bookDao.update(book);

        Book book2 = bookDao.getById(book.getId());
        assertEquals(1, book2.getAuthors().size());

        book.getAuthors().remove(author);

        bookDao.update(book);

        Book book3 = bookDao.getById(book.getId());
        assertEquals(0, book3.getAuthors().size());
    }

    @DisplayName("Удалить жанра из книги")
    @Test
    void removeGenreFormBook() {
        Book book = new Book();
        book.setName("NameVVD");
        book.setGenres(new ArrayList<>());
        book = bookDao.insert(book);

        Genre genre = new Genre();
        genre.setName("name1");
        genre = genreDao.insert(genre);

        book.getGenres().add(genre);
        bookDao.update(book);

        Book book2 = bookDao.getById(book.getId());
        assertEquals(1, book2.getGenres().size());

        book.getGenres().remove(genre);

        bookDao.update(book);

        Book book3 = bookDao.getById(book.getId());
        assertEquals(0, book3.getGenres().size());
    }
}