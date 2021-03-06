package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.BookComment;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.repository.author.AuthorDao;
import ru.otus.homework.repository.book.BookCommentDao;
import ru.otus.homework.repository.book.BookDao;
import ru.otus.homework.repository.genre.GenreDao;
import ru.otus.homework.service.performance.BookPerformance;
import ru.otus.homework.service.performance.GenrePerformance;
import ru.otus.homework.service.performance.Performance;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookCommentDao bookCommentDao;
    private final BookPerformance bookPerformance;
    private final GenrePerformance genrePerformance;
    private final Performance<Author> authorPerformance;

    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao, BookCommentDao bookCommentDao, BookPerformance bookPerformance, GenrePerformance genrePerformance, Performance<Author> authorPerformance) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookCommentDao = bookCommentDao;
        this.bookPerformance = bookPerformance;
        this.genrePerformance = genrePerformance;
        this.authorPerformance = authorPerformance;
    }

    @Transactional
    @Override
    public void add(String bookName) {
        Book book = new Book();
        book.setName(bookName);
        long id = bookDao.insert(book);
        bookPerformance.add(id);
    }

    @Transactional
    @Override
    public void addAuthor(long bookId, long authorId) {
        Book book = bookDao.getById(bookId);
        if (book != null) {
            if (getAuthorFromBook(book, authorId) != null) {
                bookPerformance.authorAlreadyAdded(bookId, authorId);
            } else {
                Author author = authorDao.getById(authorId);
                if (author != null) {
                    book.getAuthors().add(author);
                    bookDao.update(book);
                    bookPerformance.authorAdded(bookId, authorId);
                } else {
                    authorPerformance.notFound(authorId);
                }
            }
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Transactional
    @Override
    public void addGenre(long bookId, long genreId) {
        Book book = bookDao.getById(bookId);
        if (book != null) {
            if (getGenreFromBook(book, genreId) != null) {
                bookPerformance.genreAlreadyAdded(bookId, genreId);
            } else {
                Genre genre = genreDao.getById(genreId);
                if (genre != null) {
                    book.getGenres().add(genre);
                    bookDao.update(book);
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
        Book book = bookDao.getById(bookId);
        if (book != null) {
            BookComment bookComment = new BookComment();
            bookComment.setComment(comment);
            bookCommentDao.insert(bookComment);
            book.getComments().add(bookComment);
            bookDao.update(book);
            bookPerformance.commentAdded(bookId, bookComment.getId(), comment);
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Transactional
    @Override
    public void delete(long bookId) {
        if (bookDao.getById(bookId) != null) {
            bookDao.delete(bookId);
            bookPerformance.delete(bookId);
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Transactional
    @Override
    public void outputAll() {
        List<Book> books = bookDao.getAll();
        bookPerformance.total(books.size());
        for (Book book : books) {
            bookPerformance.output(book);
        }
    }

    @Transactional
    @Override
    public void outputBookComments(long bookId) {
        Book book = bookDao.getById(bookId);
        if (book != null) {
            List<BookComment> bookComments = bookCommentDao.getAll(bookId);
            bookPerformance.totalComments(bookComments.size());
            for (BookComment bookComment : bookComments) {
                bookPerformance.outputBookComment(bookComment.getId(), bookComment.getComment());
            }
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Transactional
    @Override
    public void output(long bookId) {
        Book book = bookDao.getById(bookId);
        if (book != null) {
            bookPerformance.output(book);
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Transactional
    @Override
    public void removeAuthor(long bookId, long authorId) {
        Book book = bookDao.getById(bookId);
        if (book != null) {
            Author author = getAuthorFromBook(book, authorId);
            if (author != null) {
                book.getAuthors().remove(author);
                bookDao.update(book);
                bookPerformance.authorRemoved(bookId, authorId);
            } else {
                bookPerformance.authorMissing(bookId, authorId);
            }
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Transactional
    @Override
    public void removeGenre(long bookId, long genreId) {
        Book book = bookDao.getById(bookId);
        if (book != null) {
            Genre genre = getGenreFromBook(book, genreId);
            if (genre != null) {
                book.getGenres().remove(genre);
                bookDao.update(book);
                bookPerformance.genreRemoved(bookId, genreId);
            } else {
                bookPerformance.genreMissing(bookId, genreId);
            }
        } else {
            bookPerformance.notFound(bookId);
        }
    }

    @Transactional
    @Override
    public void removeBookComment(long bookCommentId) {
        BookComment bookComment = bookCommentDao.getById(bookCommentId);
        if (bookComment == null) {
            bookPerformance.commentNotFound(bookCommentId);
        } else {
            String comment = bookComment.getComment();
            for (Book book : bookDao.getAll()) {
                if (book.getComments().contains(bookComment)) {
                    book.getComments().remove(bookComment);
                    bookDao.update(book);
                    break;
                }
            }
            bookPerformance.removeComment(bookCommentId, comment);
        }
    }

    @Transactional
    @Override
    public void updateBookComment(long bookCommentId, String comment) {
        BookComment bookComment = bookCommentDao.getById(bookCommentId);
        if (bookComment == null) {
            bookPerformance.commentNotFound(bookCommentId);
        } else {
            bookComment.setComment(comment);
            bookCommentDao.update(bookComment);
            bookPerformance.updateComment(bookCommentId, comment);
        }
    }

    @Transactional
    @Override
    public void updateBookName(long bookId, String newName) {
        Book book = bookDao.getById(bookId);
        if (book != null) {
            String oldName = book.getName();
            book.setName(newName);
            bookPerformance.updateName(bookId, oldName, newName);
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
