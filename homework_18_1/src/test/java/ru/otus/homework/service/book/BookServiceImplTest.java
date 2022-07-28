package ru.otus.homework.service.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.book.BookComment;
import ru.otus.homework.service.author.AuthorServiceImpl;
import ru.otus.homework.service.book.BookServiceImpl;
import ru.otus.homework.service.genre.GenreServiceImpl;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookServiceImpl")
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("ru.otus.homework")
class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private GenreServiceImpl genreService;

    @Test
    @DisplayName("Добавление книги")
    void add() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        assertEquals("name" + id, bookService.getById(book.getId()).getName());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Получение книги")
    void getById() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        assertEquals("name" + id, bookService.getById(book.getId()).getName());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Получение комментария")
    void getBookCommentById() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        BookComment bookComment = bookService.addComment(book, "comment" + id);
        assertEquals("comment" + id, bookService.getBookCommentById(bookComment.getId()).getComment());
        bookService.deleteComment(bookComment);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Получение всех книг")
    void getAll() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Book book1 = bookService.add("name" + id1);
        Book book2 = bookService.add("name" + id2);
        Book bookT1 = null;
        Book bookT2 = null;
        for (Book book : bookService.getAll()) {
            if (book.getName().equals("name" + id1)) {
                bookT1 = book;
            }
            if (book.getName().equals("name" + id2)) {
                bookT2 = book;
            }
            if (bookT1 != null && bookT2 != null) {
                break;
            }
        }
        assertNotNull(bookT1);
        assertNotNull(bookT2);
        bookService.delete(book1);
        bookService.delete(book2);
    }

    @Test
    @DisplayName("Редактирование наименования книги")
    void editName() {
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        assertEquals("name" + id, bookService.getById(book.getId()).getName());
        bookService.editName(book, "name" + id2);
        assertEquals("name" + id2, bookService.getById(book.getId()).getName());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Добавление автора в книгу")
    void addAuthor() {
        String aId = UUID.randomUUID().toString();
        long aId2 = Math.abs(new Random().nextLong());
        ;
        String bId = UUID.randomUUID().toString();
        Book book = bookService.add("name" + bId);

        Author author = authorService.add("surname" + aId, "name" + aId, "patronymic" + aId);
        bookService.addAuthor(book, author);
        Book bookT = bookService.getById(book.getId());
        assertTrue(bookT.getAuthors().contains(author));

        Author author2 = new Author();
        author2.setId(aId2);
        author2.setSurname("surname" + aId2);
        author2.setName("name" + aId2);
        author2.setPatronymic("patronymic" + aId2);
        bookService.addAuthor(book, author2);
        Book bookT2 = bookService.getById(book.getId());
        assertFalse(bookT2.getAuthors().contains(author2));

        bookService.delete(book);
        authorService.delete(author);
    }

    @Test
    @DisplayName("Удаление автора из книги")
    void deleteAuthor() {
        String aId = UUID.randomUUID().toString();
        String bId = UUID.randomUUID().toString();
        Book book = bookService.add("name" + bId);
        Author author = authorService.add("surname" + aId, "name" + aId, "patronymic" + aId);
        bookService.addAuthor(book, author);
        Book bookT = bookService.getById(book.getId());
        assertTrue(bookT.getAuthors().contains(author));

        bookService.deleteAuthor(book, author);
        Book bookT2 = bookService.getById(book.getId());
        assertFalse(bookT2.getAuthors().contains(author));

        bookService.delete(book);
        authorService.delete(author);
    }

    @Test
    @DisplayName("Добавление жанра в книгу")
    void addGenre() {
        String gId = UUID.randomUUID().toString();
        long gId2 = Math.abs(new Random().nextLong());
        String bId = UUID.randomUUID().toString();
        Book book = bookService.add("name" + bId);

        Genre genre = genreService.add("name" + gId);
        bookService.addGenre(book, genre);
        Book bookT = bookService.getById(book.getId());
        assertTrue(bookT.getGenres().contains(genre));

        Genre genre2 = new Genre();
        genre2.setId(gId2);
        genre2.setName("name" + gId2);
        bookService.addGenre(book, genre2);
        Book bookT2 = bookService.getById(book.getId());
        assertFalse(bookT2.getGenres().contains(genre2));

        bookService.delete(book);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Удаление жанра из книги")
    void deleteGenre() {
        String aId = UUID.randomUUID().toString();
        String bId = UUID.randomUUID().toString();
        Book book = bookService.add("name" + bId);
        Genre genre = genreService.add("name" + aId);
        bookService.addGenre(book, genre);
        Book bookT = bookService.getById(book.getId());
        assertTrue(bookT.getGenres().contains(genre));

        bookService.deleteGenre(book, genre);
        Book bookT2 = bookService.getById(book.getId());
        assertFalse(bookT2.getGenres().contains(genre));

        bookService.delete(book);
        genreService.delete(genre);
    }

    @Test
    @DisplayName("Добавление комментария")
    void addComment() {
        String cId = UUID.randomUUID().toString();
        String bId = UUID.randomUUID().toString();
        Book book = bookService.add("name" + bId);
        BookComment bookComment = bookService.addComment(book, "comment" + cId);
        Book book1 = bookService.getById(bookComment.getBook().getId());
        assertEquals(book1.getName(), book.getName());
        Book bookT = bookService.getById(book.getId());
        BookComment bookComment1 = bookService.getBookCommentById(bookT.getComments().get(0).getId());
        assertEquals("comment" + cId, bookComment1.getComment());
        bookService.deleteComment(bookComment1);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Редактирование комментария")
    void editComment() {
        String cId = UUID.randomUUID().toString();
        String cId2 = UUID.randomUUID().toString();
        String bId = UUID.randomUUID().toString();
        Book book = bookService.add("name" + bId);
        bookService.addComment(book, "comment" + cId);
        Book bookT = bookService.getById(book.getId());
        BookComment bookComment1 = bookService.getBookCommentById(bookT.getComments().get(0).getId());
        assertEquals("comment" + cId, bookComment1.getComment());
        bookService.editComment(bookComment1, "comment" + cId2);
        Book bookT2 = bookService.getById(book.getId());
        BookComment bookComment2 = bookService.getBookCommentById(bookT2.getComments().get(0).getId());
        assertEquals("comment" + cId2, bookComment2.getComment());
        bookService.deleteComment(bookComment2);
        bookService.delete(book);
    }

    @Test
    @DisplayName("Удаление комментария")
    void deleteComment() {
        String cId = UUID.randomUUID().toString();
        String bId = UUID.randomUUID().toString();
        Book book = bookService.add("name" + bId);
        bookService.addComment(book, "comment" + cId);
        Book bookT = bookService.getById(book.getId());
        BookComment bookComment1 = bookService.getBookCommentById(bookT.getComments().get(0).getId());
        assertEquals("comment" + cId, bookComment1.getComment());
        bookService.deleteComment(bookComment1);
        bookT = bookService.getById(book.getId());
        assertEquals(0, bookT.getComments().size());
        bookService.delete(book);
    }

    @Test
    @DisplayName("Удаление комментария после удаления книги")
    void deleteCommentAfterDeleteBook() {
        String cId = UUID.randomUUID().toString();
        String bId = UUID.randomUUID().toString();
        Book book = bookService.add("name" + bId);
        long commentId = bookService.addComment(book, "comment" + cId).getId();
        Book bookT = bookService.getById(book.getId());
        BookComment bookComment1 = bookService.getBookCommentById(bookT.getComments().get(0).getId());
        assertEquals("comment" + cId, bookComment1.getComment());
        bookService.delete(book);
        assertNull(bookService.getBookCommentById(commentId));
    }

    @Test
    @DisplayName("Удаление книги")
    void delete() {
        String id = UUID.randomUUID().toString();
        Book book = bookService.add("name" + id);
        long bookId = book.getId();
        assertEquals("name" + id, bookService.getById(book.getId()).getName());
        bookService.delete(book);
        assertNull(bookService.getById(bookId));
    }
}