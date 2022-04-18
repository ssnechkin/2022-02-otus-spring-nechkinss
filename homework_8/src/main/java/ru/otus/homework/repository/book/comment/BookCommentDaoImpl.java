package ru.otus.homework.repository.book.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.BookComment;
import ru.otus.homework.repository.book.BookRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookCommentDaoImpl implements BookCommentDao {

    private final BookCommentRepository bookCommentRepository;
    private final BookRepository bookRepository;

    @Override
    public void delete(String id) {
        bookCommentRepository.findById(id).ifPresent(bookCommentRepository::delete);
    }

    @Override
    public List<BookComment> getAll(String bookId) {
        return bookRepository.findById(bookId).orElse(new Book()).getComments();
    }

    @Override
    public BookComment getById(String id) {
        return bookCommentRepository.findById(id).orElse(null);
    }

    @Override
    public BookComment insert(BookComment bookComment) {
        return bookCommentRepository.save(bookComment);
    }

    @Override
    public void update(BookComment bookComment) {
        if (bookCommentRepository.findById(bookComment.getId()).isPresent()) {
            bookCommentRepository.save(bookComment);
        }
    }
}
