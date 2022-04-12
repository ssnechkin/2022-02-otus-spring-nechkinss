package ru.otus.homework.repository.author;

import org.springframework.stereotype.Component;
import ru.otus.homework.entity.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class AuthorDaoImpl implements AuthorDao {
    @PersistenceContext
    private final EntityManager em;

    public AuthorDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void delete(long id) {
        em.remove(em.find(Author.class, id));
    }

    @Override
    public List<Author> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> cq = cb.createQuery(Author.class);
        Root<Author> rootEntry = cq.from(Author.class);
        CriteriaQuery<Author> all = cq.select(rootEntry);
        TypedQuery<Author> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public Author getById(long id) {
        return em.find(Author.class, id);
    }

    @Override
    public long insert(Author object) {
        em.persist(object);
        return object.getId();
    }

    @Override
    public void update(Author object) {
        em.merge(object);
    }
}
