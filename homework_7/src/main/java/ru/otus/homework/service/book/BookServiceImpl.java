package ru.otus.homework.service.book;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.BookComment;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.repository.author.AuthorRepository;
import ru.otus.homework.repository.book.BookRepository;
import ru.otus.homework.repository.book.comment.BookCommentRepository;
import ru.otus.homework.repository.genre.GenreRepository;
import ru.otus.homework.service.performance.Performance;
import ru.otus.homework.service.performance.book.BookPerformance;
import ru.otus.homework.service.performance.genre.GenrePerformance;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookCommentRepository bookCommentRepository;
    private final BookPerformance bookPerformance;
    private final GenrePerformance genrePerformance;
    private final Performance<Author> authorPerformance;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository,
                           BookCommentRepository bookCommentRepository, BookPerformance bookPerformance,
                           GenrePerformance genrePerformance, Performance<Author> authorPerformance) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookCommentRepository = bookCommentRepository;
        this.bookPerformance = bookPerformance;
        this.genrePerformance = genrePerformance;
        this.authorPerformance = authorPerformance;
    }

    @Override
    public void add(String bookName) {
        Book book = new Book();
        book.setName(bookName);
        book = bookRepository.save(book);
        bookPerformance.add(book.getId());
    }

    @Override
    public void addAuthor(long bookId, long authorId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            if (getAuthorFromBook(book.get(), authorId) != null) {
                bookPerformance.authorAlreadyAdded(bookId, authorId);
            } else {
                Optional<Author> author = authorRepository.findById(authorId);
                if (author.isPresent()) {
                    book.get().getAuthors().add(author.get());
                    bookRepository.save(book.get());
                    bookPerformance.authorAdded(bookId, authorId);
                } else {
                    authorPerformance.notFound(authorId);
                }
            }
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Override
    public void addGenre(long bookId, long genreId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            if (getGenreFromBook(book.get(), genreId) != null) {
                bookPerformance.genreAlreadyAdded(bookId, genreId);
            } else {
                Optional<Genre> genre = genreRepository.findById(genreId);
                if (genre.isPresent()) {
                    book.get().getGenres().add(genre.get());
                    bookRepository.save(book.get());
                    bookPerformance.genreAdded(bookId, genreId);
                } else {
                    genrePerformance.notFound(genreId);
                }
            }
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Transactional
    @Override
    public void addComment(long bookId, String comment) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            BookComment bookComment = new BookComment();
            bookComment.setComment(comment);
            bookComment.setBook(book.get());
            bookCommentRepository.save(bookComment);
            book.get().getComments().add(bookComment);
            bookRepository.save(book.get());
            bookPerformance.commentAdded(bookId, bookComment.getId(), comment);
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Override
    public void delete(long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            bookPerformance.delete(bookId);
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Override
    public void outputAll() {
        List<Book> books = bookRepository.findAll();
        bookPerformance.total(books.size());
        for (Book book : books) {
            bookPerformance.output(book);
        }
    }

    @Override
    public void outputBookComments(long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            List<BookComment> bookComments = book.get().getComments();
            bookPerformance.totalComments(bookComments.size());
            for (BookComment bookComment : bookComments) {
                bookPerformance.outputBookComment(bookComment.getId(), bookComment.getComment());
            }
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Override
    public void output(long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            bookPerformance.output(book.get());
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Override
    public void removeAuthor(long bookId, long authorId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            Author author = getAuthorFromBook(book.get(), authorId);
            if (author != null) {
                book.get().getAuthors().remove(author);
                bookRepository.save(book.get());
                bookPerformance.authorRemoved(bookId, authorId);
            } else {
                bookPerformance.authorMissing(bookId, authorId);
            }
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Override
    public void removeGenre(long bookId, long genreId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            Genre genre = getGenreFromBook(book.get(), genreId);
            if (genre != null) {
                book.get().getGenres().remove(genre);
                bookRepository.save(book.get());
                bookPerformance.genreRemoved(bookId, genreId);
            } else {
                bookPerformance.genreMissing(bookId, genreId);
            }
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Override
    public void removeBookComment(long bookCommentId) {
        Optional<BookComment> bookComment = bookCommentRepository.findById(bookCommentId);
        if (bookComment.isPresent()) {
            String comment = bookComment.get().getComment();
            Book book = bookComment.get().getBook();
            book.getComments().remove(bookComment.get());
            bookRepository.save(book);
            bookCommentRepository.delete(bookComment.get());
            bookPerformance.removeComment(bookCommentId, comment);
        } else {
            bookPerformance.commentNotFound(bookCommentId);
        }
    }

    @Override
    public void updateBookComment(long bookCommentId, String comment) {
        Optional<BookComment> bookComment = bookCommentRepository.findById(bookCommentId);
        if (bookComment.isPresent()) {
            bookComment.get().setComment(comment);
            bookCommentRepository.save(bookComment.get());
            bookPerformance.updateComment(bookCommentId, comment);
        } else {
            bookPerformance.commentNotFound(bookCommentId);
        }
    }

    @Override
    public void updateBookName(long bookId, String newName) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            book.get().setName(newName);
            bookRepository.save(book.get());
            bookPerformance.edit(bookId, book.get());
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    private Author getAuthorFromBook(Book book, long authorId) {
        List<Author> authors = book.getAuthors();
        for (Author author : authors) {
            if (author.getId() == authorId) {
                return author;
            }
        }
        return null;
    }

    private Genre getGenreFromBook(Book book, long genreId) {
        List<Genre> genres = book.getGenres();
        for (Genre genre : genres) {
            if (genre.getId() == genreId) {
                return genre;
            }
        }
        return null;
    }
}
