package ru.otus.homework.repository.relational.book.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.relational.book.BookComment;

public interface BookCommentRepositoryRel extends JpaRepository<BookComment, Long> {
}
