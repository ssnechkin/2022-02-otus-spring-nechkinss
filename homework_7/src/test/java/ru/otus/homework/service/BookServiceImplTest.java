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
import ru.otus.homework.repository.author.AuthorRepository;
import ru.otus.homework.repository.book.BookRepository;
import ru.otus.homework.repository.book.comment.BookCommentRepository;
import ru.otus.homework.repository.genre.GenreRepository;
import ru.otus.homework.service.author.AuthorServiceImpl;
import ru.otus.homework.service.book.BookServiceImpl;
import ru.otus.homework.service.genre.GenreServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookServiceImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private GenreServiceImpl genreService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @Test
    void add() {
        bookService.add("Name");
    }

    @Test
    void addAuthor() {
        Book book = new Book();
        book.setName("NameY");
        book = bookRepository.save(book);

        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        author = authorRepository.save(author);

        bookService.addAuthor(book.getId(), author.getId());
        assertTrue(authorRepository.getById(author.getId()).getBooks().contains(book));
    }

    @Test
    void addGenre() {
        Book book = new Book();
        book.setName("NameY");
        book = bookRepository.save(book);

        Genre genre = new Genre();
        genre.setName("NameS");
        genre = genreRepository.save(genre);

        bookService.addGenre(book.getId(), genre.getId());
        assertTrue(genreRepository.getById(genre.getId()).getBooks().contains(book));
    }

    @Test
    void addComment() {
        Book book = new Book();
        book.setName("NameY");
        book = bookRepository.save(book);
        bookService.addComment(book.getId(), "comment");
        assertEquals(1, bookRepository.findById(book.getId()).get().getComments().size());
    }

    @Test
    void delete() {
        Book book = new Book();
        book.setName("NameY");
        book = bookRepository.save(book);
        long id = book.getId();
        bookService.delete(book.getId());
        assertFalse(bookRepository.findById(id).isPresent());
    }

    @Test
    void outputAll() {
        bookService.outputAll();
    }

    @Test
    void output() {
        Book book = new Book();
        book.setName("NameY");
        book = bookRepository.save(book);
        bookService.output(book.getId());
    }

    @Test
    void removeAuthor() {
        Book book = new Book();
        book.setName("NameY");
        book = bookRepository.save(book);

        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        author = authorRepository.save(author);

        bookService.addAuthor(book.getId(), author.getId());
        assertTrue(book.getAuthors().contains(author));
        assertTrue(author.getBooks().contains(book));

        bookService.removeAuthor(book.getId(), author.getId());
        assertFalse(book.getAuthors().contains(author));
        assertFalse(author.getBooks().contains(book));
    }

    @Test
    void removeGenre() {
        Book book = new Book();
        book.setName("NameY");
        book = bookRepository.save(book);

        Genre genre = new Genre();
        genre.setName("NameS");
        genre = genreRepository.save(genre);

        bookService.addGenre(book.getId(), genre.getId());
        assertTrue(book.getGenres().contains(genre));
        assertTrue(genre.getBooks().contains(book));

        bookService.removeGenre(book.getId(), genre.getId());
        assertFalse(book.getGenres().contains(genre));
        assertFalse(genre.getBooks().contains(book));
    }

    @Test
    void updateComment() {
        BookComment bookComment = new BookComment();
        bookComment.setComment("comment1");
        bookComment = bookCommentRepository.save(bookComment);

        Book book = new Book();
        book.setName("NameY");
        book.getComments().add(bookComment);
        bookRepository.save(book);

        bookService.updateBookComment(bookComment.getId(), "comment2");
        assertEquals("comment2", bookRepository.getById(book.getId()).getComments().get(0).getComment());
    }

    @Test
    void updateName() {
        Book book = new Book();
        book.setName("NameY");
        book = bookRepository.save(book);
        bookService.updateBookName(book.getId(), "Name2");
        assertEquals("Name2", bookRepository.getById(book.getId()).getName());
    }

    @Test
    void removeComment() {
        Book book = new Book();
        book.setName("NameY");
        bookRepository.save(book);

        BookComment bookComment = new BookComment();
        bookComment.setComment("comment1");
        bookComment.setBook(book);
        bookComment = bookCommentRepository.save(bookComment);

        book.getComments().add(bookComment);
        bookRepository.save(book);

        long commentId = bookComment.getId();

        bookService.removeBookComment(bookComment.getId());
        assertFalse(bookCommentRepository.findById(commentId).isPresent());
        book = bookRepository.getById(book.getId());
        assertEquals(0, book.getComments().size());
    }

    @DisplayName("Удалить автора и автора из всех книг")
    @Test
    void removeAuthorAndAuthorFormAllBooks() {
        Book book = new Book();
        book.setName("NameVVD");
        book = bookRepository.save(book);

        Author author = new Author();
        author.setName("name1");
        author.getBooks().add(book);
        author = authorRepository.save(author);

        book.getAuthors().add(author);
        bookRepository.save(book);

        Book book2 = bookRepository.getById(book.getId());
        assertEquals(1, book2.getAuthors().size());

        Author author1 = authorRepository.getById(author.getId());
        assertEquals(1, author1.getBooks().size());

        authorService.delete(author.getId());

        Book book3 = bookRepository.getById(book.getId());
        assertEquals(0, book3.getAuthors().size());
    }

    @DisplayName("Удалить жанра и жанра из всех книг")
    @Test
    void removeGenreAndGenreFormAllBooks() {
        Book book = new Book();
        book.setName("NameVVD");
        book = bookRepository.save(book);

        Genre genre = new Genre();
        genre.setName("name1");
        genre.getBooks().add(book);
        genre = genreRepository.save(genre);

        book.getGenres().add(genre);
        bookRepository.save(book);

        Book book2 = bookRepository.getById(book.getId());
        assertEquals(1, book2.getGenres().size());

        Genre genre1 = genreRepository.getById(genre.getId());
        assertEquals(1, genre1.getBooks().size());

        genreService.delete(genre.getId());

        Book book3 = bookRepository.getById(book.getId());
        assertEquals(0, book3.getGenres().size());
    }
}