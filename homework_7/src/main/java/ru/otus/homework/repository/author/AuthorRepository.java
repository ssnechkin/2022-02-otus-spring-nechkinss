package ru.otus.homework.repository.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.homework.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{
}
