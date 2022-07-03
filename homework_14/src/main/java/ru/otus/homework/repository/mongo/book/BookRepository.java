package ru.otus.homework.repository.mongo.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.mongo.book.Book;

public interface BookRepository extends MongoRepository<Book, String> {
}
