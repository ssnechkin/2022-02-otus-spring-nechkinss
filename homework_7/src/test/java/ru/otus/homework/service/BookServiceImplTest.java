package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.BookComment;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.repository.author.AuthorDaoImpl;
import ru.otus.homework.repository.book.BookDaoImpl;
import ru.otus.homework.repository.book.comment.BookCommentDaoImpl;
import ru.otus.homework.repository.genre.GenreDaoImpl;

@DisplayName("Класс BookServiceImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookDaoImpl bookDao;

    @Autowired
    private AuthorDaoImpl authorDao;

    @Autowired
    private GenreDaoImpl genreDao;

    @Autowired
    private BookCommentDaoImpl bookCommentDao;

    @Test
    void add() {
        bookService.add("Name");
    }

    @Test
    void addAuthor() {
        Book book = new Book();
        book.setName("NameY");
        long booId = bookDao.insert(book).getId();

        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        long authorId = authorDao.insert(author).getId();

        bookService.addAuthor(booId, authorId);
    }

    @Test
    void addGenre() {
        Book book = new Book();
        book.setName("NameY");
        long booId = bookDao.insert(book).getId();

        Genre genre = new Genre();
        genre.setName("NameS");
        long genreId = genreDao.insert(genre).getId();

        bookService.addGenre(booId, genreId);
    }

    @Test
    void addComment() {
        Book book = new Book();
        book.setName("NameY");
        long booId = bookDao.insert(book).getId();
        bookService.addComment(booId, "comment");
    }

    @Test
    void delete() {
        Book book = new Book();
        book.setName("NameY");
        long booId = bookDao.insert(book).getId();
        bookService.delete(booId);
    }

    @Test
    void outputAll() {
        bookService.outputAll();
    }

    @Test
    void output() {
        Book book = new Book();
        book.setName("NameY");
        long booId = bookDao.insert(book).getId();
        bookService.output(booId);
    }

    @Test
    void removeAuthor() {
        Book book = new Book();
        book.setName("NameY");
        long booId = bookDao.insert(book).getId();

        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        long authorId = authorDao.insert(author).getId();

        bookService.addAuthor(booId, authorId);
        bookService.removeAuthor(booId, authorId);
    }

    @Test
    void removeGenre() {
        Book book = new Book();
        book.setName("NameY");
        long booId = bookDao.insert(book).getId();

        Genre genre = new Genre();
        genre.setName("NameS");
        long genreId = genreDao.insert(genre).getId();

        bookService.addGenre(booId, genreId);
        bookService.removeGenre(booId, genreId);
    }

    @Test
    void updateComment() {
        BookComment bookComment = new BookComment();
        bookComment.setComment("comment1");
        bookComment = bookCommentDao.insert(bookComment);

        Book book = new Book();
        book.setName("NameY");
        book.getComments().add(bookComment);
        bookDao.insert(book);

        bookService.updateBookComment(bookComment.getId(), "comment2");
    }

    @Test
    void updateName() {
        Book book = new Book();
        book.setName("NameY");
        long booId = bookDao.insert(book).getId();
        bookService.updateBookName(booId, "Name2");
    }

    @Test
    void removeComment() {
        BookComment bookComment = new BookComment();
        bookComment.setComment("comment1");
        bookComment = bookCommentDao.insert(bookComment);

        Book book = new Book();
        book.setName("NameY");
        book.getComments().add(bookComment);
        bookDao.insert(book);

        bookService.removeBookComment(bookComment.getId());
    }
}