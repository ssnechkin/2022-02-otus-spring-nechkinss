package ru.otus.homework.shell.publisher;

public interface EventsPublisher {
    void outputBook(long bookId);

    void outputAllBooks();

    void addBook(String bookName);

    void deleteBookById(long bookId);

    void addAnAuthorToABook(long bookId, long authorId);

    void addAnGenreToABook(long bookId, long genreId);

    void addAnBookCommentToABook(long bookId, String comment);

    void removeTheAuthorFromTheBook(long bookId, long authorId);

    void removeTheGenreFromTheBook(long bookId, long genreId);

    void removeTheBookCommentFromTheBook(long bookCommentId);

    void outputAllAuthors();

    void addAuthor(String surname, String name, String patronymic);

    void deleteAuthorById(long authorId);

    void outputAllGenres();

    void addGenre(String genreName);

    void deleteGenreById(long genreId);

    void setGenreDescription(long genreId, String description);

    void updateBookComment(long bookCommentId, String comment);

    void updateBookName(long bookId, String name);

    void outputBookComments(long bookId);
}
