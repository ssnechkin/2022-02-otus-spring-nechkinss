package ru.otus.homework.shell.publisher;

public interface EventsPublisher {
    void outputBook(String bookId);

    void outputAllBooks();

    void addBook(String bookName);

    void deleteBookById(String bookId);

    void addAnAuthorToABook(String bookId, String authorId);

    void addAnGenreToABook(String bookId, String genreId);

    void addAnBookCommentToABook(String bookId, String comment);

    void removeTheAuthorFromTheBook(String bookId, String authorId);

    void removeTheGenreFromTheBook(String bookId, String genreId);

    void removeTheBookCommentFromTheBook(String bookCommentId);

    void outputAllAuthors();

    void addAuthor(String surname, String name, String patronymic);

    void deleteAuthorById(String authorId);

    void outputAllGenres();

    void addGenre(String genreName);

    void deleteGenreById(String genreId);

    void setGenreDescription(String genreId, String description);

    void updateBookComment(String bookCommentId, String comment);

    void updateBookName(String bookId, String name);

    void outputBookComments(String bookId);

    void editAuthor(String authorId, String surname, String name, String patronymic);

    void editGenre(String genreId, String genreName);
}
