package ru.otus.homework.service.performance.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.domain.book.Book;
import ru.otus.homework.domain.book.BookComment;
import ru.otus.homework.repository.author.AuthorRepository;
import ru.otus.homework.repository.book.BookRepository;
import ru.otus.homework.repository.book.comment.BookCommentRepository;
import ru.otus.homework.repository.genre.GenreRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookPerformanceImpl")
@DataMongoTest
@ComponentScan("ru.otus.homework")
class BookPerformanceImplTest {

    @Autowired
    private BookPerformanceImpl bookPerformance;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @Test
    @DisplayName("Добавление книги")
    void addBook() {
        String id = UUID.randomUUID().toString();
        bookPerformance.add("name" + id);
        Book bookT = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null
                    && book.getName() != null
                    && book.getName().equals("name" + id)) {
                bookT = book;
                break;
            }
        }
        assertNotNull(bookT);
        bookRepository.delete(bookT);
    }

    @Test
    @DisplayName("Получение книги")
    void book() {
        String id = UUID.randomUUID().toString();
        bookPerformance.add("name" + id);
        Book bookT = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null
                    && book.getName() != null
                    && book.getName().equals("name" + id)) {
                bookT = book;
                break;
            }
        }
        assertNotNull(bookT);
        String bId = bookT.getId();
        assertNotNull(bookPerformance.getById(bId));
        assertTrue(bookPerformance.getById(bId).contains(id));
        bookRepository.delete(bookT);
    }

    @Test
    @DisplayName("Получение всех книг")
    void books() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        bookPerformance.add("name" + id);
        bookPerformance.add("name" + id2);
        String bookT1 = null;
        String bookT2 = null;
        for (String line : bookPerformance.getAll()) {
            if (line != null) {
                if (line.contains("name" + id)) {
                    bookT1 = line;
                }
                if (line.contains("name" + id2)) {
                    bookT2 = line;
                }
                if (bookT1 != null && bookT2 != null) {
                    break;
                }
            }
        }
        assertNotNull(bookT1);
        assertNotNull(bookT2);

        Book book1 = null;
        Book book2 = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null && book.getName() != null) {
                if (book.getName().equals("name" + id)) {
                    book1 = book;
                }
                if (book.getName().equals("name" + id2)) {
                    book2 = book;
                }
                if (book1 != null && book2 != null) {
                    break;
                }
            }
        }
        if (book1 != null)
            bookRepository.delete(book1);
        if (book2 != null)
            bookRepository.delete(book2);
    }

    @Test
    @DisplayName("Редактироваине наименования книги")
    void editBookName() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        bookPerformance.add("name" + id);
        Book bookT = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null
                    && book.getName() != null
                    && book.getName().equals("name" + id)) {
                bookT = book;
                break;
            }
        }
        assertNotNull(bookT);

        bookPerformance.edit(bookT.getId(), "name" + id2);
        bookT = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null
                    && book.getName() != null
                    && book.getName().equals("name" + id2)) {
                bookT = book;
                break;
            }
        }
        assertNotNull(bookT);
        bookRepository.delete(bookT);
    }

    @Test
    @DisplayName("Добавление комментария")
    void addBookComment() {
        String id = UUID.randomUUID().toString();
        bookPerformance.add("name" + id);
        Book bookT = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null
                    && book.getName() != null
                    && book.getName().equals("name" + id)) {
                bookT = book;
                break;
            }
        }
        assertNotNull(bookT);
        bookPerformance.addComment(bookT.getId(), "comment" + id);
        bookT = bookRepository.findById(bookT.getId()).get();
        String commentId = bookT.getCommentIdList().get(0);
        BookComment bookComment = bookCommentRepository.findById(commentId).get();
        assertEquals("comment" + id, bookComment.getComment());
        bookCommentRepository.delete(bookComment);
        bookRepository.delete(bookT);
    }

    @Test
    @DisplayName("Получение всех комментариев")
    void bookComments() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        bookPerformance.add("name" + id);
        Book bookT = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null
                    && book.getName() != null
                    && book.getName().equals("name" + id)) {
                bookT = book;
                break;
            }
        }
        assertNotNull(bookT);
        bookPerformance.addComment(bookT.getId(), "comment" + id);
        bookPerformance.addComment(bookT.getId(), "comment" + id2);
        bookT = bookRepository.findById(bookT.getId()).get();

        String commentId = bookT.getCommentIdList().get(0);
        BookComment bookComment = bookCommentRepository.findById(commentId).get();
        String commentId2 = bookT.getCommentIdList().get(1);
        BookComment bookComment2 = bookCommentRepository.findById(commentId2).get();

        String comment1 = null;
        String comment2 = null;
        for (String line : bookPerformance.getComments(bookT.getId())) {
            if (line.contains("comment" + id)) {
                comment1 = line;
            }
            if (line.contains("comment" + id2)) {
                comment2 = line;
            }
        }
        assertNotNull(comment1);
        assertNotNull(comment2);

        bookCommentRepository.delete(bookComment);
        bookCommentRepository.delete(bookComment2);
        bookRepository.delete(bookT);
    }

    @Test
    @DisplayName("Редактирование комментария")
    void editBookComment() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        bookPerformance.add("name" + id);
        Book bookT = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null
                    && book.getName() != null
                    && book.getName().equals("name" + id)) {
                bookT = book;
                break;
            }
        }
        assertNotNull(bookT);
        bookPerformance.addComment(bookT.getId(), "comment" + id);
        bookT = bookRepository.findById(bookT.getId()).get();
        String commentId = bookT.getCommentIdList().get(0);
        BookComment bookComment = bookCommentRepository.findById(commentId).get();
        assertEquals("comment" + id, bookComment.getComment());

        bookPerformance.editCommentByCommentId(bookComment.getId(), "comment" + id2);
        bookComment = bookCommentRepository.findById(commentId).get();
        assertEquals("comment" + id2, bookComment.getComment());

        bookCommentRepository.delete(bookComment);
        bookRepository.delete(bookT);
    }

    @Test
    @DisplayName("Удаление комментария")
    void deleteBookComment() {
        String id = UUID.randomUUID().toString();
        bookPerformance.add("name" + id);
        Book bookT = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null
                    && book.getName() != null
                    && book.getName().equals("name" + id)) {
                bookT = book;
                break;
            }
        }
        assertNotNull(bookT);
        bookPerformance.addComment(bookT.getId(), "comment" + id);
        bookT = bookRepository.findById(bookT.getId()).get();
        String commentId = bookT.getCommentIdList().get(0);
        BookComment bookComment = bookCommentRepository.findById(commentId).get();
        assertEquals("comment" + id, bookComment.getComment());

        bookPerformance.deleteCommentByCommentId(commentId);
        assertFalse(bookCommentRepository.findById(commentId).isPresent());
        bookRepository.delete(bookT);
    }

    @Test
    @DisplayName("Добавление автора в книгу")
    void addAnAuthorToABook() {
        String bid = UUID.randomUUID().toString();
        Book book = new Book();
        book.setName("name" + bid);
        book = bookRepository.save(book);

        String aId2 = UUID.randomUUID().toString();
        Author author = new Author();
        author.setSurname("surname" + aId2);
        author.setName("name" + aId2);
        author.setPatronymic("patronymic" + aId2);
        author = authorRepository.save(author);

        bookPerformance.addAuthor(book.getId(), author.getId());

        book = bookRepository.findById(book.getId()).get();
        assertTrue(book.getAuthorIdList().contains(author.getId()));

        author = authorRepository.findById(author.getId()).get();
        assertTrue(author.getBookIdList().contains(book.getId()));
        authorRepository.delete(author);
        bookRepository.delete(book);
    }

    @Test
    @DisplayName("Удаление автора из книги")
    void deleteAuthorFromBook() {
        String bid = UUID.randomUUID().toString();
        Book book = new Book();
        book.setName("name" + bid);
        book = bookRepository.save(book);

        String aId2 = UUID.randomUUID().toString();
        Author author = new Author();
        author.setSurname("surname" + aId2);
        author.setName("name" + aId2);
        author.setPatronymic("patronymic" + aId2);
        author = authorRepository.save(author);

        bookPerformance.addAuthor(book.getId(), author.getId());

        book = bookRepository.findById(book.getId()).get();
        assertEquals(book.getAuthorIdList().get(0), author.getId());
        author = authorRepository.findById(author.getId()).get();
        assertTrue(author.getBookIdList().contains(book.getId()));

        bookPerformance.deleteAuthor(book.getId(), author.getId());

        book = bookRepository.findById(book.getId()).get();
        assertFalse(book.getAuthorIdList().contains(author.getId()));
        author = authorRepository.findById(author.getId()).get();
        assertFalse(author.getBookIdList().contains(book.getId()));

        authorRepository.delete(author);
        bookRepository.delete(book);
    }

    @Test
    @DisplayName("Добавление жанра в книгу")
    void addAnBookToABook() {
        String bid = UUID.randomUUID().toString();
        Book book = new Book();
        book.setName("name" + bid);
        book = bookRepository.save(book);

        String gId2 = UUID.randomUUID().toString();
        Genre genre = new Genre();
        genre.setName("name" + gId2);
        genre = genreRepository.save(genre);

        bookPerformance.addGenre(book.getId(), genre.getId());

        book = bookRepository.findById(book.getId()).get();
        assertTrue(book.getGenreIdList().contains(genre.getId()));

        genre = genreRepository.findById(genre.getId()).get();
        assertTrue(genre.getBookIdList().contains(book.getId()));
        genreRepository.delete(genre);
        bookRepository.delete(book);
    }

    @Test
    @DisplayName("Удаление жанра из книги")
    void deleteBookFromBook() {
        String bid = UUID.randomUUID().toString();
        Book book = new Book();
        book.setName("name" + bid);
        book = bookRepository.save(book);

        String aId2 = UUID.randomUUID().toString();
        Genre genre = new Genre();
        genre.setName("name" + aId2);
        genre = genreRepository.save(genre);

        bookPerformance.addGenre(book.getId(), genre.getId());

        book = bookRepository.findById(book.getId()).get();
        assertTrue(book.getGenreIdList().contains(genre.getId()));
        genre = genreRepository.findById(genre.getId()).get();
        assertTrue(genre.getBookIdList().contains(book.getId()));

        bookPerformance.deleteGenre(book.getId(), genre.getId());

        book = bookRepository.findById(book.getId()).get();
        assertFalse(book.getGenreIdList().contains(genre.getId()));
        genre = genreRepository.findById(genre.getId()).get();
        assertFalse(genre.getBookIdList().contains(book.getId()));

        genreRepository.delete(genre);
        bookRepository.delete(book);
    }

    @Test
    @DisplayName("Удаление книги")
    void deleteBookById() {
        String id = UUID.randomUUID().toString();
        bookPerformance.add("name" + id);
        Book bookT = null;
        for (Book book : bookRepository.findAll()) {
            if (book != null
                    && book.getName() != null
                    && book.getName().equals("name" + id)) {
                bookT = book;
                break;
            }
        }
        assertNotNull(bookT);
        String delId = bookT.getId();
        bookPerformance.deleteById(delId);
        assertFalse(bookRepository.findById(delId).isPresent());
    }
}