package ru.otus.homework.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.mongo.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {
}
