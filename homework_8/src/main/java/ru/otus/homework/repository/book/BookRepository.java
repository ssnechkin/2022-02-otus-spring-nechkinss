package ru.otus.homework.repository.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.book.Book;

public interface BookRepository extends MongoRepository<Book, String> {
}
