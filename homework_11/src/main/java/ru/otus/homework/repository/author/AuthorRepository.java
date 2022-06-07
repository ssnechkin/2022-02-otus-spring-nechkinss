package ru.otus.homework.repository.author;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.homework.domain.entity.author.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
