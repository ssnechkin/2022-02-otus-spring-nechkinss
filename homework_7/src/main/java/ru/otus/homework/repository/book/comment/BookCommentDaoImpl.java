package ru.otus.homework.repository.book.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.homework.entity.BookComment;
import ru.otus.homework.repository.book.BookRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookCommentDaoImpl implements BookCommentDao {

    private final BookCommentRepository bookCommentRepository;
    private final BookRepository bookRepository;

    @Override
    public void delete(long id) {
        bookCommentRepository.findById(id).ifPresent(bookCommentRepository::delete);
    }

    @Override
    public List<BookComment> getAll(long bookId) {
        return bookRepository.getById(bookId).getComments();
    }

    @Override
    public BookComment getById(long id) {
        return bookCommentRepository.getById(id);
    }

    @Override
    public BookComment insert(BookComment bookComment) {
        return bookCommentRepository.save(bookComment);
    }

    @Override
    public void update(BookComment bookComment) {
        bookCommentRepository.findById(bookComment.getId()).ifPresent(bookCommentRepository::save);
    }
}
