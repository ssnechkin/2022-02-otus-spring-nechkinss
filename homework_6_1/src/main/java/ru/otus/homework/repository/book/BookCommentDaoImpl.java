package ru.otus.homework.repository.book;

import org.springframework.stereotype.Repository;
import ru.otus.homework.entity.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BookCommentDaoImpl implements BookCommentDao {
    @PersistenceContext
    private final EntityManager entityManager;

    public BookCommentDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long count(long bookId) {
        Query query = entityManager.createQuery("SELECT count(1) FROM BookComment bc WHERE bc.bookId = :bookId", Long.class);
        query.setParameter("bookId", bookId);
        Long count = (Long) query.getSingleResult();
        return count == null ? 0 : count;
    }

    @Override
    public void delete(long id) {
        Query query = entityManager.createQuery("DELETE BookComment WHERE id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<BookComment> getAll(long bookId) {
        TypedQuery<BookComment> query = entityManager.createQuery("SELECT bc FROM BookComment bc WHERE bc.bookId = :bookId", BookComment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public List<BookComment> getById(long id) {
        TypedQuery<BookComment> query = entityManager.createQuery("SELECT bc FROM BookComment bc WHERE bc.id = :id", BookComment.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public long insert(BookComment object) {
        entityManager.persist(object);
        return object.getId();
    }

    @Override
    public void update(BookComment object) {
        Query query = entityManager.createQuery("UPDATE BookComment bc SET bc.comment = :comment WHERE bc.id = :id");
        query.setParameter("id", object.getId());
        query.setParameter("comment", object.getComment());
        query.executeUpdate();
    }
}
