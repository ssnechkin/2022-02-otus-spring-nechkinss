package ru.otus.homework.repository.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.homework.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

    private final BookRepository bookRepository;

    @Override
    public void delete(String id) {
        bookRepository.findById(id).ifPresent(bookRepository::delete);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book insert(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void update(Book book) {
        if (bookRepository.findById(book.getId()).isPresent()) {
            bookRepository.save(book);
        }
    }
}
