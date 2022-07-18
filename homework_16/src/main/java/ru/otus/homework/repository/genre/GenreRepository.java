package ru.otus.homework.repository.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.entity.genre.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
