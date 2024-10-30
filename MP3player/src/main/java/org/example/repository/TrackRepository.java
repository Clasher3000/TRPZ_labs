package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.Track;
import org.example.entity.Track_;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class TrackRepository {

    private SessionFactory sessionFactory;

    public TrackRepository() {
        // Ініціалізація SessionFactory в конструкторі
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // конфігурація з hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Не вдалося створити SessionFactory", e);
        }

    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public Session openSession() {
        return sessionFactory.openSession();
    }

    public List<Track> getAll() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Track> tracks = entityManager.createQuery("SELECT t from Track t", Track.class).getResultList();

        entityManager.getTransaction().commit();
        return tracks;
    }

    public Track getTrackByTitle(String title) {

        EntityManager entityManager = sessionFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Track> criteriaQuery = criteriaBuilder.createQuery(Track.class);
        Root<Track> root = criteriaQuery.from(Track.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Track_.TITLE), title));

        TypedQuery<Track> query = entityManager.createQuery(criteriaQuery);
        Track result = query.getSingleResult();


        entityManager.close();
        return result;
    }

}
