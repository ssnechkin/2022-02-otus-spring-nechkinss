package ru.otus.homework.service.performance.book;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.domain.book.Book;
import ru.otus.homework.domain.book.BookComment;
import ru.otus.homework.service.texter.author.AuthorTexter;
import ru.otus.homework.service.texter.book.BookTexter;
import ru.otus.homework.service.texter.genre.GenreTexter;
import ru.otus.homework.service.author.AuthorService;
import ru.otus.homework.service.book.BookService;
import ru.otus.homework.service.genre.GenreService;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookPerformanceImpl implements BookPerformance {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookTexter bookTexter;
    private final AuthorTexter authorTexter;
    private final GenreTexter genreTexter;

    public BookPerformanceImpl(BookService bookService, AuthorService authorService, GenreService genreService, BookTexter bookTexter, AuthorTexter authorTexter, GenreTexter genreTexter) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookTexter = bookTexter;
        this.authorTexter = authorTexter;
        this.genreTexter = genreTexter;
    }

    @Override
    public String add(String name) {
        Book book = bookService.add(name);
        return bookTexter.add(book);
    }

    @Override
    public String getById(String id) {
        Book book = bookService.getById(id);
        return bookTexter.toString(book,
                getAuthorList(book.getAuthorIdList()),
                getGenreList(book.getGenreIdList()));
    }

    @Override
    public List<String> getAll() {
        List<Book> bookList = bookService.getAll();
        ArrayList<String> result = new ArrayList<>();
        result.add(bookTexter.total(bookList.size()));
        for (Book book : bookList) {
            result.add(bookTexter.toString(book,
                    getAuthorList(book.getAuthorIdList()),
                    getGenreList(book.getGenreIdList())));
        }
        return result;
    }

    @Override
    public String edit(String id, String name) {
        Book book = bookService.getById(id);
        if (book == null) {
            return bookTexter.notFound(id);
        } else {
            book = bookService.editName(book, name);
            return bookTexter.edit(book,
                    getAuthorList(book.getAuthorIdList()),
                    getGenreList(book.getGenreIdList()));
        }
    }

    @Override
    public String addComment(String id, String comment) {
        Book book = bookService.getById(id);
        if (book == null) {
            return bookTexter.notFound(id);
        } else {
            BookComment bookComment = bookService.addComment(book, comment);
            return bookTexter.commentAdded(book, bookComment);
        }
    }

    @Override
    public List<String> getComments(String id) {
        Book book = bookService.getById(id);
        ArrayList<String> result = new ArrayList<>();
        if (book == null) {
            result.add(bookTexter.notFound(id));
        } else {
            result.add(bookTexter.totalComments(book.getCommentIdList().size()));
            for (String bookCommentId : book.getCommentIdList()) {
                BookComment bookComment = bookService.getBookCommentById(bookCommentId);
                result.add(bookTexter.outputBookComment(bookComment));
            }
        }
        return result;
    }

    @Override
    public String editCommentByCommentId(String commentId, String comment) {
        BookComment bookComment = bookService.getBookCommentById(commentId);
        if (bookComment == null) {
            return bookTexter.commentNotFound(commentId);
        } else {
            bookComment = bookService.editComment(bookComment, comment);
            return bookTexter.updateComment(bookComment, comment);
        }
    }

    @Override
    public String deleteCommentByCommentId(String commentId) {
        BookComment bookComment = bookService.getBookCommentById(commentId);
        if (bookComment == null) {
            return bookTexter.commentNotFound(commentId);
        } else {
            String comment = bookComment.getComment();
            bookService.deleteComment(bookComment);
            return bookTexter.deleteComment(commentId, comment);
        }
    }

    @Override
    public String addAuthor(String id, String authorId) {
        Book book = bookService.getById(id);
        if (book == null) {
            return bookTexter.notFound(id);
        } else {
            Author author = authorService.getById(authorId);
            if (author == null) {
                return authorTexter.notFound(authorId);
            } else {
                if (bookService.addAuthor(book, author)) {
                    return bookTexter.authorAdded(book, author);
                } else {
                    return bookTexter.authorAlreadyAdded(book, author);
                }
            }
        }
    }

    @Override
    public String deleteAuthor(String id, String authorId) {
        Book book = bookService.getById(id);
        if (book == null) {
            return bookTexter.notFound(id);
        } else {
            Author author = authorService.getById(authorId);
            if (author == null) {
                return authorTexter.notFound(authorId);
            } else {
                if (bookService.deleteAuthor(book, author)) {
                    return bookTexter.deleteAuthor(book, author);
                } else {
                    return bookTexter.authorMissing(book, author);
                }
            }
        }
    }

    @Override
    public String addGenre(String id, String genreId) {
        Book book = bookService.getById(id);
        if (book == null) {
            return bookTexter.notFound(id);
        } else {
            Genre genre = genreService.getById(genreId);
            if (genre == null) {
                return genreTexter.notFound(genreId);
            } else {
                if (bookService.addGenre(book, genre)) {
                    return bookTexter.genreAdded(book, genre);
                } else {
                    return bookTexter.genreAlreadyAdded(book, genre);
                }
            }
        }
    }

    @Override
    public String deleteGenre(String id, String genreId) {
        Book book = bookService.getById(id);
        if (book == null) {
            return bookTexter.notFound(id);
        } else {
            Genre genre = genreService.getById(genreId);
            if (genre == null) {
                return genreTexter.notFound(genreId);
            } else {
                if (bookService.deleteGenre(book, genre)) {
                    return bookTexter.deleteGenre(book, genre);
                } else {
                    return bookTexter.genreMissing(book, genre);
                }
            }
        }
    }

    @Override
    public String deleteById(String id) {
        Book book = bookService.getById(id);
        if (book == null) {
            return bookTexter.notFound(id);
        } else {
            bookService.delete(book);
            return bookTexter.delete(id);
        }
    }

    private List<Author> getAuthorList(List<String> authorIdList) {
        List<Author> authorList = new ArrayList<>();
        for (String authorId : authorIdList) {
            Author author = authorService.getById(authorId);
            if (author != null) {
                authorList.add(author);
            }
        }
        return authorList;
    }

    private List<Genre> getGenreList(List<String> genreIdList) {
        List<Genre> genreList = new ArrayList<>();
        for (String genreId : genreIdList) {
            Genre genre = genreService.getById(genreId);
            if (genre != null) {
                genreList.add(genre);
            }
        }
        return genreList;
    }
}