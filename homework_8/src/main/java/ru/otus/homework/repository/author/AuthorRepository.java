package ru.otus.homework.repository.author;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.entity.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {
}
