package ru.otus.homework.repository.book;

import org.springframework.stereotype.Component;
import ru.otus.homework.entity.Book;
import ru.otus.homework.entity.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class BookCommentDaoImpl implements BookCommentDao {
    @PersistenceContext
    private final EntityManager em;

    public BookCommentDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void delete(long id) {
        em.remove(em.find(BookComment.class, id));
    }

    @Override
    public List<BookComment> getAll(long bookId) {
        return em.find(Book.class, bookId).getComments();
    }

    @Override
    public BookComment getById(long id) {
        return em.find(BookComment.class, id);
    }

    @Override
    public long insert(BookComment object) {
        em.persist(object);
        return object.getId();
    }

    @Override
    public void update(BookComment object) {
        em.merge(object);
    }
}
