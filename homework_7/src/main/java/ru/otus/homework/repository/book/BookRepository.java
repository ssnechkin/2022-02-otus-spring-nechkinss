package ru.otus.homework.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.homework.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
