package ru.otus.homework.repository.mongo.book.comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.mongo.book.BookComment;

public interface BookCommentRepository extends MongoRepository<BookComment, String> {
}
