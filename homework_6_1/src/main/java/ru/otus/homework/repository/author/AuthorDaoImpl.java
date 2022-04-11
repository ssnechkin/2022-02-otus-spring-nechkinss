package ru.otus.homework.repository.author;

import org.springframework.stereotype.Repository;
import ru.otus.homework.entity.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AuthorDaoImpl implements AuthorDao {
    @PersistenceContext
    private final EntityManager entityManager;

    public AuthorDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long count() {
        Query query = entityManager.createQuery("SELECT count(1) FROM Author", Long.class);
        Long count = (Long) query.getSingleResult();
        return count == null ? 0 : count;
    }

    @Override
    public void delete(long id) {
        Query query = entityManager.createQuery("DELETE Author WHERE id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public List<Author> getById(long id) {
        TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a WHERE a.id = :id", Author.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public long insert(Author object) {
        entityManager.persist(object);
        return object.getId();
    }

    @Override
    public void update(Author object) {
        entityManager.merge(object);
    }
}
