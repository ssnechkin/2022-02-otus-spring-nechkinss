package ru.otus.homework.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.entity.book.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
