package ru.otus.homework.repository.author;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.entity.author.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
