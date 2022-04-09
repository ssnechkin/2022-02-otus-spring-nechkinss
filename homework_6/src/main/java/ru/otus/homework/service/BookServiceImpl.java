package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.entity.Author;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.Genre;
import ru.otus.homework.repository.author.AuthorDao;
import ru.otus.homework.repository.book.BookDao;
import ru.otus.homework.repository.genre.GenreDao;
import ru.otus.homework.service.io.IOServiceStreams;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final IOServiceStreams ioService;

    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao, IOServiceStreams ioService) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.ioService = ioService;
    }

    @Override
    @Transactional
    public void add(String bookName) {
        Book book = new Book();
        book.setName(bookName);
        long id = bookDao.insert(book);
        ioService.outputString("Book added. ID: " + id);
    }

    @Override
    @Transactional
    public void addAuthor(long bookId, long authorId) {
        List<Book> books = bookDao.getById(bookId);
        if (books.size() == 0) {
            ioService.outputString("The book was not found by ID: " + bookId);
        } else {
            Book book = books.get(0);
            if (getAuthorFromBook(book, authorId) != null) {
                ioService.outputString("The author has already been added to the selected book ID: " + bookId + " Author ID: " + authorId);
            } else {
                List<Author> authorList = authorDao.getById(authorId);
                if (authorList.size() > 0) {
                    if (book.getAuthor() == null) {
                        book.setAuthor(new ArrayList<>());
                    }
                    book.getAuthor().add(authorList.get(0));
                    bookDao.update(book);
                    ioService.outputString("The author added to the selected book ID: " + bookId + " Author ID: " + authorId);
                } else {
                    ioService.outputString("The author not found. ID: " + authorId);
                }
            }
        }
    }

    @Override
    @Transactional
    public void addGenre(long bookId, long genreId) {
        List<Book> books = bookDao.getById(bookId);
        if (books.size() == 0) {
            ioService.outputString("The book was not found by ID: " + bookId);
        } else {
            Book book = books.get(0);
            if (getGenreFromBook(book, genreId) != null) {
                ioService.outputString("The genre has already been added to the selected book ID: " + bookId + " Genre ID: " + genreId);
            } else {
                List<Genre> genreList = genreDao.getById(genreId);
                if (genreList.size() > 0) {
                    if (book.getGenre() == null) {
                        book.setGenre(new ArrayList<>());
                    }
                    book.getGenre().add(genreList.get(0));
                    bookDao.update(book);
                    ioService.outputString("The genre added to the selected book ID: " + bookId + " Genre ID: " + genreId);
                } else {
                    ioService.outputString("The genre not found. ID: " + genreId);
                }
            }
        }
    }

    @Override
    @Transactional
    public void delete(long bookId) {
        List<Book> books = bookDao.getById(bookId);
        if (books.size() == 0) {
            ioService.outputString("The book was not found by ID: " + bookId);
        } else {
            bookDao.delete(bookId);
            ioService.outputString("Book deleted. ID: " + bookId);
        }
    }

    @Override
    @Transactional
    public void outputAll() {
        List<Book> books = bookDao.getAll();
        ioService.outputString("Total books: " + bookDao.count());
        for (Book book : books) {
            outputBook(book);
        }
    }

    @Override
    @Transactional
    public void output(long bookId) {
        List<Book> books = bookDao.getById(bookId);
        if (books.size() == 0) {
            ioService.outputString("The book was not found by ID: " + bookId);
        } else {
            outputBook(books.get(0));
        }
    }

    @Override
    @Transactional
    public void removeAuthor(long bookId, long authorId) {
        List<Book> books = bookDao.getById(bookId);
        if (books.size() == 0) {
            ioService.outputString("The book was not found by ID: " + bookId);
        } else {
            Book book = books.get(0);
            Author author = getAuthorFromBook(book, authorId);
            if (author != null) {
                book.getAuthor().remove(author);
                bookDao.update(book);
                ioService.outputString("The author has been removed from the book. BookId: " + bookId + " AuthorId: " + authorId);
            } else {
                ioService.outputString("The author with the ID=" + authorId + " is missing from the book ID: " + bookId);
            }
        }
    }

    @Override
    @Transactional
    public void removeGenre(long bookId, long genreId) {
        List<Book> books = bookDao.getById(bookId);
        if (books.size() == 0) {
            ioService.outputString("The book was not found by ID: " + bookId);
        } else {
            Book book = books.get(0);
            Genre genre = getGenreFromBook(book, genreId);
            if (genre != null) {
                book.getGenre().remove(genre);
                bookDao.update(book);
                ioService.outputString("The genre has been removed from the book. BookId: " + bookId + " GenreId: " + genreId);
            } else {
                ioService.outputString("The genre with the ID=" + genreId + " is missing from the book ID: " + bookId);
            }
        }
    }

    @Override
    @Transactional
    public void setComment(long bookId, String comment) {
        List<Book> books = bookDao.getById(bookId);
        if (books.size() == 0) {
            ioService.outputString("The book was not found by ID: " + bookId);
        } else {
            Book book = books.get(0);
            book.setComment(comment);
            bookDao.update(book);
            ioService.outputString("Book comment is set. ID: " + bookId + " Comment: " + comment);
        }
    }

    private Author getAuthorFromBook(Book book, long authorId) {
        List<Author> authors = book.getAuthor();
        if (authors != null) {
            for (Author author : authors) {
                if (author.getId() == authorId) {
                    return author;
                }
            }
        }
        return null;
    }

    private Genre getGenreFromBook(Book book, long genreId) {
        List<Genre> genres = book.getGenre();
        if (genres != null) {
            for (Genre genre : genres) {
                if (genre.getId() == genreId) {
                    return genre;
                }
            }
        }
        return null;
    }

    private void outputBook(Book book) {
        ioService.outputString("Book"
                + " ID: " + book.getId()
                + " Name: " + book.getName()
                + " Authors: " + getAuthors(book)
                + " Genres: " + getGenres(book)
        );
    }

    private String getAuthors(Book book) {
        StringBuilder authors = new StringBuilder();
        if (book.getAuthor() != null) {
            for (Author author : book.getAuthor()) {
                authors.append("id: ").append(author.getId()).append(" ")
                        .append(author.getSurname()).append(" ")
                        .append(author.getName()).append(" ")
                        .append(author.getPatronymic()).append("; ");
            }
        }
        return authors.toString();
    }

    private String getGenres(Book book) {
        StringBuilder genres = new StringBuilder();
        if (book.getGenre() != null) {
            for (Genre genre : book.getGenre()) {
                genres.append("id: ").append(genre.getId()).append(" ")
                        .append(genre.getName()).append(" ")
                        .append(genre.getDescription()).append("; ");
            }
        }
        return genres.toString();
    }
}
