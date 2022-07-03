package ru.otus.homework.repository.relational.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.relational.Genre;

import java.util.List;

public interface GenreRepositoryRel extends JpaRepository<Genre, Long> {
    List<Genre> findByNameAndDescription(String name, String description);
}
