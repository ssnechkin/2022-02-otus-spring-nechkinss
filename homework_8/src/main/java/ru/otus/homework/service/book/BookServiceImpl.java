package ru.otus.homework.service.book;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.domain.book.Book;
import ru.otus.homework.domain.book.BookComment;
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
    public Book getById(String id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    @Override
    public BookComment getBookCommentById(String id) {
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
        if (author.getId() == null
                || book.getAuthorIdList().contains(author.getId())
                || authorRepository.findById(author.getId()).isEmpty()) {
            return false;
        } else {
            book.getAuthorIdList().add(author.getId());
            bookRepository.save(book);
            author.getBookIdList().add(book.getId());
            authorRepository.save(author);
            return true;
        }
    }

    @Transactional
    @Override
    public boolean deleteAuthor(Book book, Author author) {
        if (book.getAuthorIdList().contains(author.getId())) {
            book.getAuthorIdList().remove(author.getId());
            bookRepository.save(book);
            author.getBookIdList().remove(book.getId());
            authorRepository.save(author);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean addGenre(Book book, Genre genre) {
        if (genre.getId() == null
                || book.getGenreIdList().contains(genre.getId())
                || genreRepository.findById(genre.getId()).isEmpty()) {
            return false;
        } else {
            book.getGenreIdList().add(genre.getId());
            bookRepository.save(book);
            genre.getBookIdList().add(book.getId());
            genreRepository.save(genre);
            return true;
        }
    }

    @Override
    public boolean deleteGenre(Book book, Genre genre) {
        if (book.getGenreIdList().contains(genre.getId())) {
            book.getGenreIdList().remove(genre.getId());
            bookRepository.save(book);
            genre.getBookIdList().remove(book.getId());
            genreRepository.save(genre);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public BookComment addComment(Book book, String comment) {
        BookComment bookComment = new BookComment();
        bookComment.setComment(comment);
        bookComment.setBookId(book.getId());
        bookComment = bookCommentRepository.save(bookComment);
        book.getCommentIdList().add(bookComment.getId());
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
        String bookId = bookComment.getBookId();
        if (bookId != null) {
            Book book = bookRepository.findById(bookId).get();
            book.getCommentIdList().remove(bookComment.getId());
            bookRepository.save(book);
        }
        bookCommentRepository.delete(bookComment);
    }

    @Transactional
    @Override
    public void delete(Book book) {
        deleteComments(book);
        deleteLinkAuthors(book);
        deleteLinkGenres(book);
        bookRepository.delete(book);
    }

    private void deleteComments(Book book) {
        for (String id : book.getCommentIdList()) {
            if (id != null) {
                Optional<BookComment> optional = bookCommentRepository.findById(id);
                optional.ifPresent(bookCommentRepository::delete);
            }
        }
    }

    private void deleteLinkAuthors(Book book) {
        for (String id : book.getAuthorIdList()) {
            if (id != null) {
                Optional<Author> optional = authorRepository.findById(id);
                if (optional.isPresent()) {
                    optional.get().getBookIdList().remove(book.getId());
                    authorRepository.save(optional.get());
                }
            }
        }
    }

    private void deleteLinkGenres(Book book) {
        for (String id : book.getGenreIdList()) {
            if (id != null) {
                Optional<Genre> optional = genreRepository.findById(id);
                if (optional.isPresent()) {
                    optional.get().getBookIdList().remove(book.getId());
                    genreRepository.save(optional.get());
                }
            }
        }
    }
}
