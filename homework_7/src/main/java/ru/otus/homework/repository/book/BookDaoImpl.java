package ru.otus.homework.repository.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.homework.entity.Book;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

    private final BookRepository bookRepository;

    @Override
    public void delete(long id) {
        bookRepository.findById(id).ifPresent(bookRepository::delete);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(long id) {
        return bookRepository.getById(id);
    }

    @Override
    public Book insert(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void update(Book book) {
        bookRepository.findById(book.getId()).ifPresent(bookRepository::save);
    }
}
