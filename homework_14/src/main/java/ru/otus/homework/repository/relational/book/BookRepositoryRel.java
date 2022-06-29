package ru.otus.homework.repository.relational.book;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.relational.book.Book;

public interface BookRepositoryRel extends JpaRepository<Book, Long> {
}
