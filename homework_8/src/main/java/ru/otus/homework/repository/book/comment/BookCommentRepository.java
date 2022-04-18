package ru.otus.homework.repository.book.comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.entity.BookComment;

public interface BookCommentRepository extends MongoRepository<BookComment, String> {
}
