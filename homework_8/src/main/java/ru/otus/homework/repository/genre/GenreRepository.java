package ru.otus.homework.repository.genre;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {
}
