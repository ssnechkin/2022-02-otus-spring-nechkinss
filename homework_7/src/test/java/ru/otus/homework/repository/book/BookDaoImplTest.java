package ru.otus.homework.repository.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.BookComment;
import ru.otus.homework.repository.author.AuthorRepository;
import ru.otus.homework.repository.book.comment.BookCommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс BookDaoImpl")
@DataJpaTest
@ComponentScan("ru.otus.homework")
class BookDaoImplTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @DisplayName("Добавление")
    @Test
    void insert() {
        Book book = new Book();
        book.setName("NameY");
        book = bookRepository.save(book);
        Book book1 = bookRepository.getById(book.getId());
        assertEquals(book1.getName(), "NameY");
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Book book = new Book();
        book.setName("NameV");
        book = bookRepository.save(book);
        book.setName("NameF");
        bookRepository.save(book);
        Book book1 = bookRepository.getById(book.getId());
        assertEquals("NameF", book1.getName());
    }

    @DisplayName("Добавить комментарий")
    @Test
    void addComment() {
        Book book = new Book();
        book.setName("NameV");
        book = bookRepository.save(book);

        ArrayList<BookComment> bookComments = new ArrayList<>();
        BookComment bookComment = new BookComment();
        String uuid = UUID.randomUUID().toString();
        bookComment.setComment(uuid);
        bookCommentRepository.save(bookComment);
        bookComments.add(bookComment);
        book.setComments(bookComments);
        bookRepository.save(book);

        Book book1 = bookRepository.getById(book.getId());
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
        book = bookRepository.save(book);
        ArrayList<BookComment> bookComments = new ArrayList<>();
        BookComment bookComment = new BookComment();
        bookComment.setComment(comment1);
        bookCommentRepository.save(bookComment);
        bookComments.add(bookComment);
        book.setComments(bookComments);
        bookRepository.save(book);
        Book book1 = bookRepository.getById(book.getId());
        for (BookComment bookComment2 : book1.getComments()) {
            if (bookComment2.getComment() != null && bookComment2.getComment().equals(comment1)) {
                bookComment2.setComment(comment2);
                bookCommentRepository.save(bookComment2);
                break;
            }
        }
        Book book2 = bookRepository.getById(book.getId());
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
        book = bookRepository.save(book);
        book.setName(name2);
        bookRepository.save(book);
        Book book2 = bookRepository.getById(book.getId());
        assertEquals(book2.getName(), name2);
    }

    @DisplayName("Удаление комментария")
    @Test
    void deleteComment() {
        Book book = new Book();
        book.setName("NameV");
        bookRepository.save(book);

        ArrayList<BookComment> bookComments = new ArrayList<>();
        BookComment bookComment = new BookComment();
        String uuid = UUID.randomUUID().toString();
        bookComment.setComment(uuid);
        bookComments.add(bookComment);
        book.setComments(bookComments);
        bookRepository.save(book);

        book.getComments().remove(bookComment);
        bookRepository.save(book);
    }

    @DisplayName("Удаление")
    @Test
    void delete() {
        Book book1 = new Book();
        book1.setName("Name_delete1");
        bookRepository.save(book1);
        Book book2 = new Book();
        book2.setName("Name_delete2");
        book2 = bookRepository.save(book2);
        bookRepository.delete(book2);

        boolean name1Del = true;
        boolean name2Del = true;
        for (Book book : bookRepository.findAll()) {
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
        bookRepository.save(book);
        List<Book> books = bookRepository.findAll();
        assertTrue(books.size() >= 1);
    }

    @DisplayName("Получение одного")
    @Test
    void getById() {
        Book book = new Book();
        book.setName("NameM");
        book = bookRepository.save(book);
        Book book1 = bookRepository.getById(book.getId());
        assertEquals("NameM", book1.getName());
    }

    @DisplayName("Удалить автора из книги")
    @Test
    void removeAuthorFormBook() {
        Book book = new Book();
        book.setName("NameVVD");
        book.setAuthors(new ArrayList<>());
        book = bookRepository.save(book);

        Author author = new Author();
        author.setSurname("Surname");
        author.setName("NameY");
        author.setPatronymic("Patronymic");
        author = authorRepository.save(author);

        Author author1 = authorRepository.getById(author.getId());

        Book book1 = bookRepository.getById(book.getId());
        book1.getAuthors().add(author1);
        bookRepository.save(book1);

        Book book2 = bookRepository.getById(book.getId());
        assertEquals(1, book2.getAuthors().size());
        book.getAuthors().remove(author);

        Book book3 = bookRepository.getById(book.getId());
        assertEquals(0, book3.getAuthors().size());
    }
}