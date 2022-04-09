package ru.otus.homework.repository.genre;

import org.springframework.stereotype.Repository;
import ru.otus.homework.entity.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class GenreDaoImpl implements GenreDao {
    @PersistenceContext
    private final EntityManager entityManager;

    public GenreDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long count() {
        Query query = entityManager.createQuery("SELECT count(1) FROM Genre", Long.class);
        Long count = (Long) query.getSingleResult();
        return count == null ? 0 : count;
    }

    @Override
    public void delete(long id) {
        Query query = entityManager.createQuery("DELETE Genre WHERE id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = entityManager.createQuery("SELECT g FROM Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public List<Genre> getById(long id) {
        TypedQuery<Genre> query = entityManager.createQuery("SELECT g FROM Genre g WHERE g.id = :id", Genre.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public long insert(Genre object) {
        entityManager.persist(object);
        return object.getId();
    }

    @Override
    public void update(Genre object) {
        Query query = entityManager.createQuery("UPDATE Genre set name = :name, description = :description WHERE id = :id");
        query.setParameter("id", object.getId());
        query.setParameter("name", object.getName());
        query.setParameter("description", object.getDescription());
        query.executeUpdate();
    }
}
