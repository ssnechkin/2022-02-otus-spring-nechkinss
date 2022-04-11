package ru.otus.homework.repository.book;

import org.springframework.stereotype.Repository;
import ru.otus.homework.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BookDaoImpl implements BookDao {
    @PersistenceContext
    private final EntityManager entityManager;

    public BookDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long count() {
        Query query = entityManager.createQuery("SELECT count(1) FROM Book", Long.class);
        Long count = (Long) query.getSingleResult();
        return count == null ? 0 : count;
    }

    @Override
    public void delete(long id) {
        Book book = entityManager.find(Book.class, id);
        entityManager.remove(book);
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b", Book.class);
        return query.getResultList();
    }

    @Override
    public List<Book> getById(long id) {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b WHERE b.id = :id", Book.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public long insert(Book object) {
        entityManager.persist(object);
        return object.getId();
    }

    @Override
    public void update(Book object) {
        entityManager.merge(object);
    }
}
