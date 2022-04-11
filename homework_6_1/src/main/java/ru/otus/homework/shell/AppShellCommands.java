package ru.otus.homework.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.shell.publisher.EventsPublisher;

@ShellComponent
public class AppShellCommands {
    private final EventsPublisher eventsPublisher;
    private Long bookId;

    public AppShellCommands(EventsPublisher eventsPublisher) {
        this.eventsPublisher = eventsPublisher;
    }

    @ShellMethod(value = "output the selected book", key = {"b", "output-book"})
    @ShellMethodAvailability(value = "isEmptyBookId")
    public void OutputTheSelectedBook() {
        eventsPublisher.outputBook(bookId);
    }

    @ShellMethod(value = "Output all books", key = {"opb", "output-books"})
    public void outputAllBooks() {
        eventsPublisher.outputAllBooks();
    }

    @ShellMethod(value = "Add a book. Accepts the name of the book", key = {"ab", "add-book"})
    public void addBook(@ShellOption String bookName) {
        eventsPublisher.addBook(bookName);
    }

    @ShellMethod(value = "Delete a book by ID", key = {"dbi", "delete-book-id"})
    @ShellMethodAvailability(value = "isEmptyBookId")
    public void deleteBookById() {
        eventsPublisher.deleteBookById(bookId);
        this.bookId = null;
    }

    @ShellMethod(value = "Add an author to a book. Accepts author id", key = {"aba", "add-book-author"})
    @ShellMethodAvailability(value = "isEmptyBookId")
    public void addAnAuthorToABook(@ShellOption long authorId) {
        eventsPublisher.addAnAuthorToABook(bookId, authorId);
    }

    @ShellMethod(value = "Output all authors", key = {"opa", "output-authors"})
    public void outputAllAuthors() {
        eventsPublisher.outputAllAuthors();
    }

    @ShellMethod(value = "Add an author. Accepts surname, name, patronymic", key = {"aa", "add-author"})
    public void addAuthor(@ShellOption String surname, @ShellOption String name, @ShellOption String patronymic) {
        eventsPublisher.addAuthor(surname, name, patronymic);
    }

    @ShellMethod(value = "Delete a author by ID", key = {"dai", "delete-author-id"})
    public void deleteAuthorById(@ShellOption long authorId) {
        eventsPublisher.deleteAuthorById(authorId);
    }

    @ShellMethod(value = "Output all genres", key = {"opg", "output-genres"})
    public void outputAllGenres() {
        eventsPublisher.outputAllGenres();
    }

    @ShellMethod(value = "Add an genre. Accepts genreName", key = {"ag", "add-genre"})
    public void addGenre(@ShellOption String genreName) {
        eventsPublisher.addGenre(genreName);
    }

    @ShellMethod(value = "Delete a genre by ID", key = {"dgi", "delete-genre-id"})
    public void deleteGenre(@ShellOption long genreId) {
        eventsPublisher.deleteGenreById(genreId);
    }

    @ShellMethod(value = "Set genre description. Accepts genre ID and description", key = {"sgd", "set-genre-description"})
    public void setGenreDescription(@ShellOption long genreId, @ShellOption String description) {
        eventsPublisher.setGenreDescription(genreId, description);
    }

    @ShellMethod(value = "Add an genre to a book. Accepts genre id", key = {"abg", "add-book-genre"})
    @ShellMethodAvailability(value = "isEmptyBookId")
    public void addAnGenreToABook(@ShellOption long genreId) {
        eventsPublisher.addAnGenreToABook(bookId, genreId);
    }

    @ShellMethod(value = "Remove the author from the book. Accepts author id", key = {"rba", "remove-book-auth"})
    @ShellMethodAvailability(value = "isEmptyBookId")
    public void removeTheAuthorFromTheBook(@ShellOption long authorId) {
        eventsPublisher.removeTheAuthorFromTheBook(bookId, authorId);
    }

    @ShellMethod(value = "Remove the genre from the book. Accepts genre id", key = {"rbg", "remove-book-genre"})
    @ShellMethodAvailability(value = "isEmptyBookId")
    public void removeTheGenreFromTheBook(@ShellOption long genreId) {
        eventsPublisher.removeTheGenreFromTheBook(bookId, genreId);
    }

    @ShellMethod(value = "Add an comment to a book. Accepts comment", key = {"abc", "add-book-comment"})
    @ShellMethodAvailability(value = "isEmptyBookId")
    public void addAnCommentToABook(@ShellOption String comment) {
        eventsPublisher.addAnBookCommentToABook(bookId, comment);
    }

    @ShellMethod(value = "Update book comment. Accepts BookCommentID and comment", key = {"ubc", "update-book-comment"})
    public void updateBookComment(@ShellOption long bookCommentId, @ShellOption String comment) {
        eventsPublisher.updateBookComment(bookCommentId, comment);
    }

    @ShellMethod(value = "Remove the comment from the book. Accepts BookCommentID", key = {"rbc", "remove-book-comment"})
    @ShellMethodAvailability(value = "isEmptyBookId")
    public void removeTheCommentFromTheBook(@ShellOption long bookCommentId) {
        eventsPublisher.removeTheBookCommentFromTheBook(bookCommentId);
    }

    @ShellMethod(value = "Output all comments of the book", key = {"opbc", "output-book-comments"})
    @ShellMethodAvailability(value = "isEmptyBookId")
    public void outputBookComments() {
        eventsPublisher.outputBookComments(bookId);
    }

    @ShellMethod(value = "Select a book by ID", key = {"sb", "select-book"})
    public void selectABook(@ShellOption long bookId) {
        this.bookId = bookId;
    }

    private Availability isEmptyBookId() {
        return bookId == null
                ? Availability.unavailable("You need to select a book. Command: output-books AND select-book")
                : Availability.available();
    }
}
