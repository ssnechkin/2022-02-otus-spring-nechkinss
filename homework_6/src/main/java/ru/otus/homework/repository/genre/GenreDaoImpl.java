package ru.otus.homework.repository.genre;

import org.springframework.stereotype.Component;
import ru.otus.homework.entity.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    @PersistenceContext
    private final EntityManager em;

    public GenreDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void delete(long id) {
        em.remove(em.find(Genre.class, id));
    }

    @Override
    public List<Genre> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Genre> cq = cb.createQuery(Genre.class);
        Root<Genre> rootEntry = cq.from(Genre.class);
        CriteriaQuery<Genre> all = cq.select(rootEntry);
        TypedQuery<Genre> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public Genre getById(long id) {
        return em.find(Genre.class, id);
    }

    @Override
    public long insert(Genre object) {
        em.persist(object);
        return object.getId();
    }

    @Override
    public void update(Genre object) {
        em.merge(object);
    }
}
