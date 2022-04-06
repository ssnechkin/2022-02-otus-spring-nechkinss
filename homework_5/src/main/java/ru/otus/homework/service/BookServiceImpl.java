package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.config.IOServiceStreams;
import ru.otus.homework.dao.author.AuthorDao;
import ru.otus.homework.dao.book.BookAuthorDao;
import ru.otus.homework.dao.book.BookDao;
import ru.otus.homework.dao.book.BookGenreDao;
import ru.otus.homework.dao.genre.GenreDao;
import ru.otus.homework.domain.*;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookAuthorDao bookAuthorDao;
    private final BookGenreDao bookGenreDao;
    private final IOServiceStreams ioService;

    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao, BookAuthorDao bookAuthorDao, BookGenreDao bookGenreDao, IOServiceStreams ioService) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookAuthorDao = bookAuthorDao;
        this.bookGenreDao = bookGenreDao;
        this.ioService = ioService;
    }

    @Override
    public void add(String bookName) {
        long id = bookDao.generateId();
        bookDao.insert(new Book(id, bookName));
        ioService.outputString("Book added. ID: " + id);
    }

    @Override
    public void addAuthor(long bookId, long authorId) {
        if (getBookOrOutPutNotFound(bookId) != null) {
            if (bookAuthorDao.isExist(bookId, authorId)) {
                ioService.outputString("The author has already been added to the selected book ID: " + bookId + " Author ID: " + authorId);
            } else {
                if (isExistAuthor(authorId)) {
                    bookAuthorDao.insert(new BookAuthor(bookId, authorId));
                    ioService.outputString("The author added to the selected book ID: " + bookId + " Author ID: " + authorId);
                } else {
                    ioService.outputString("The author not found. ID: " + authorId);
                }
            }
        }
    }

    @Override
    public void addGenre(long bookId, long genreId) {
        if (getBookOrOutPutNotFound(bookId) != null) {
            if (bookGenreDao.isExist(bookId, genreId)) {
                ioService.outputString("The genre has already been added to the selected book ID: " + bookId + " Genre ID: " + genreId);
            } else {
                if (isExistGenre(genreId)) {
                    bookGenreDao.insert(new BookGenre(bookId, genreId));
                    ioService.outputString("The genre added to the selected book ID: " + bookId + " Genre ID: " + genreId);
                } else {
                    ioService.outputString("The genre not found. ID: " + genreId);
                }
            }
        }
    }

    @Override
    public void delete(long bookId) {
        if (getBookOrOutPutNotFound(bookId) != null) {
            bookDao.delete(bookId);
            ioService.outputString("Book deleted. ID: " + bookId);
        }
    }

    @Override
    public void outputAll() {
        List<Book> books = bookDao.getAll();
        ioService.outputString("Total books: " + bookDao.count());
        for (Book book : books) {
            outputBook(book);
        }
    }

    @Override
    public void output(long bookId) {
        Book book = getBookOrOutPutNotFound(bookId);
        if (book != null) {
            outputBook(book);
        }
    }

    @Override
    public void removeAuthor(long bookId, long authorId) {
        if (bookAuthorDao.isExist(bookId, authorId)) {
            bookAuthorDao.delete(bookId, authorId);
            ioService.outputString("The author has been removed from the book. BookId: " + bookId + " AuthorId: " + authorId);
        } else {
            ioService.outputString("The author with the ID=" + authorId + " is missing from the book ID: " + bookId);
        }
    }

    @Override
    public void removeGenre(long bookId, long genreId) {
        if (bookGenreDao.isExist(bookId, genreId)) {
            bookGenreDao.delete(bookId, genreId);
            ioService.outputString("The genre has been removed from the book. BookId: " + bookId + " GenreId: " + genreId);
        } else {
            ioService.outputString("The genre with the ID=" + genreId + " is missing from the book ID: " + bookId);
        }
    }

    private boolean isExistGenre(long genreId) {
        for (Genre genre : genreDao.getAll()) {
            if (genre.getId() == genreId) {
                return true;
            }
        }
        return false;
    }

    private boolean isExistAuthor(long authorId) {
        for (Author author : authorDao.getAll()) {
            if (author.getId() == authorId) {
                return true;
            }
        }
        return false;
    }

    private Book getBookOrOutPutNotFound(long bookId) {
        List<Book> books = bookDao.getById(bookId);
        if (books.size() == 0) {
            ioService.outputString("The book was not found by ID: " + bookId);
            return null;
        }
        return books.get(0);
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
        for (BookAuthor bookAuthor : bookAuthorDao.getListById(book.getId())) {
            List<Author> authorList = authorDao.getById(bookAuthor.getAuthorsId());
            if (authorList.size() > 0) {
                Author author = authorList.get(0);
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
        for (BookGenre bookGenre : bookGenreDao.getListById(book.getId())) {
            List<Genre> genreList = genreDao.getById(bookGenre.getGenresId());
            if (genreList.size() > 0) {
                Genre genre = genreList.get(0);
                genres.append("id: ").append(genre.getId()).append(" ")
                        .append(genre.getName()).append("; ");
            }
        }
        return genres.toString();
    }
}
