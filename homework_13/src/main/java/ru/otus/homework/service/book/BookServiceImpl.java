package ru.otus.homework.service.book;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.entity.author.Author;
import ru.otus.homework.domain.entity.book.Book;
import ru.otus.homework.domain.entity.book.BookComment;
import ru.otus.homework.domain.entity.genre.Genre;
import ru.otus.homework.dto.out.content.Button;
import ru.otus.homework.dto.out.content.Link;
import ru.otus.homework.repository.author.AuthorRepository;
import ru.otus.homework.repository.book.BookRepository;
import ru.otus.homework.repository.book.comment.BookCommentRepository;
import ru.otus.homework.repository.genre.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookCommentRepository bookCommentRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository,
                           BookCommentRepository bookCommentRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookCommentRepository = bookCommentRepository;
    }

    @Override
    public Book add(String name) {
        Book book = new Book();
        book.setName(name);
        return bookRepository.save(book);
    }

    @Override
    public Book getById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    @Override
    public BookComment getBookCommentById(long id) {
        Optional<BookComment> bookComment = bookCommentRepository.findById(id);
        return bookComment.orElse(null);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book editName(Book book, String name) {
        book.setName(name);
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public boolean addAuthor(Book book, Author author) {
        if (author == null
                || book.getAuthors().contains(author)
                || authorRepository.findById(author.getId()).isEmpty()) {
            return false;
        } else {
            book.getAuthors().add(author);
            bookRepository.save(book);
            author.getBooks().add(book);
            authorRepository.save(author);
            return true;
        }
    }

    @Transactional
    @Override
    public boolean deleteAuthor(Book book, Author author) {
        if (book.getAuthors().contains(author)) {
            book.getAuthors().remove(author);
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean addGenre(Book book, Genre genre) {
        if (genre == null
                || book.getGenres().contains(genre)
                || genreRepository.findById(genre.getId()).isEmpty()) {
            return false;
        } else {
            book.getGenres().add(genre);
            bookRepository.save(book);
            genre.getBooks().add(book);
            genreRepository.save(genre);
            return true;
        }
    }

    @Override
    public boolean deleteGenre(Book book, Genre genre) {
        if (book.getGenres().contains(genre)) {
            book.getGenres().remove(genre);
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public BookComment addComment(Book book, String comment) {
        BookComment bookComment = new BookComment();
        bookComment.setComment(comment);
        bookComment.setBook(book);
        bookComment = bookCommentRepository.save(bookComment);
        book.getComments().add(bookComment);
        bookRepository.save(book);
        return bookComment;
    }

    @Override
    public BookComment editComment(BookComment bookComment, String comment) {
        bookComment.setComment(comment);
        return bookCommentRepository.save(bookComment);
    }

    @Transactional
    @Override
    public void deleteComment(BookComment bookComment) {
        Long bookId = bookComment.getBook().getId();
        Book book = bookRepository.findById(bookId).get();
        book.getComments().remove(bookComment);
        bookRepository.save(book);
        bookCommentRepository.delete(bookComment);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }
}
