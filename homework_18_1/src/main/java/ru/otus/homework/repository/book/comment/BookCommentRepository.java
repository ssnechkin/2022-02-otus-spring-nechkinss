package ru.otus.homework.repository.book.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.entity.book.BookComment;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
}
