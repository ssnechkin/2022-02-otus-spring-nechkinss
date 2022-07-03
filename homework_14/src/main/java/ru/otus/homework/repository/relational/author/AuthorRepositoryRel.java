package ru.otus.homework.repository.relational.author;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.relational.Author;

import java.util.List;

public interface AuthorRepositoryRel extends JpaRepository<Author, Long> {
    List<Author> findByNameAndSurnameAndPatronymic(String name, String surname, String patronymic);
}
