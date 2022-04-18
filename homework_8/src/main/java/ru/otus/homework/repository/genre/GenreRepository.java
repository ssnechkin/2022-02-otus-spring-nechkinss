package ru.otus.homework.repository.genre;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.entity.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {
}
