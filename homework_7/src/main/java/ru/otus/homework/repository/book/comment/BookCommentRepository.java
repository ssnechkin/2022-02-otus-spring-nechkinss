package ru.otus.homework.repository.book.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.homework.entity.BookComment;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
}
