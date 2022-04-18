package ru.otus.homework.service;

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
import ru.otus.homework.repository.book.BookDaoImpl;
import ru.otus.homework.repository.book.comment.BookCommentDaoImpl;
import ru.otus.homework.repository.genre.GenreDaoImpl;
import ru.otus.homework.service.author.AuthorServiceImpl;
import ru.otus.homework.service.book.BookServiceImpl;
import ru.otus.homework.service.genre.GenreServiceImpl;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс BookServiceImpl")
@DataMongoTest
@ComponentScan("ru.otus.homework")
class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookDaoImpl bookDao;

    @Autowired
    private AuthorDaoImpl authorDao;

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private GenreServiceImpl genreService;

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
        String booId = bookDao.insert(book).getId();

        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        author = authorDao.insert(author);

        bookService.addAuthor(booId, author.getId());
    }

    @Test
    void addGenre() {
        Book book = new Book();
        book.setName("NameY");
        book = bookDao.insert(book);

        Genre genre = new Genre();
        genre.setName("NameS");
        genre = genreDao.insert(genre);

        bookService.addGenre(book.getId(), genre.getId());
    }

    @Test
    void addComment() {
        Book book = new Book();
        book.setName("NameY");
        book = bookDao.insert(book);
        bookService.addComment(book.getId(), "comment");
    }

    @Test
    void delete() {
        Book book = new Book();
        book.setName("NameY");
        book = bookDao.insert(book);
        bookService.delete(book.getId());
    }

    @Test
    void outputAll() {
        bookService.outputAll();
    }

    @Test
    void output() {
        Book book = new Book();
        book.setName("NameY");
        String booId = bookDao.insert(book).getId();
        bookService.output(booId);
    }

    @Test
    void removeAuthor() {
        Book book = new Book();
        book.setName("NameY");
        book = bookDao.insert(book);

        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        author = authorDao.insert(author);

        bookService.addAuthor(book.getId(), author.getId());
        bookService.removeAuthor(book.getId(), author.getId());
    }

    @Test
    void removeGenre() {
        Book book = new Book();
        book.setName("NameY");
        book = bookDao.insert(book);

        Genre genre = new Genre();
        genre.setName("NameS");
        genre = genreDao.insert(genre);

        bookService.addGenre(book.getId(), genre.getId());
        bookService.removeGenre(book.getId(), genre.getId());
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
        book = bookDao.insert(book);
        bookService.updateBookName(book.getId(), "Name2");
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

    @DisplayName("Удалить автора и автора из всех книг")
    @Test
    void removeAuthorAndAuthorFormAllBooks() {
        Book book = new Book();
        book.setName("NameVVD");
        book.setGenres(new ArrayList<>());
        book = bookDao.insert(book);

        Author author = new Author();
        author.setName("name1");
        author = authorDao.insert(author);

        book.getAuthors().add(author);
        bookDao.update(book);

        Book book2 = bookDao.getById(book.getId());
        assertEquals(1, book2.getAuthors().size());

        authorService.delete(author.getId());

        Book book3 = bookDao.getById(book.getId());
        assertEquals(0, book3.getAuthors().size());
    }

    @DisplayName("Удалить жанра и жанра из всех книг")
    @Test
    void removeGenreAndGenreFormAllBooks() {
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

        genreService.delete(genre.getId());

        Book book3 = bookDao.getById(book.getId());
        assertEquals(0, book3.getGenres().size());
    }
}