package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.Playlist;
import org.example.entity.Playlist_;
import org.example.entity.Track;
import org.example.entity.Track_;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class PlaylistRepository {
    private SessionFactory sessionFactory;
    public PlaylistRepository() {
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

    public Playlist getPlaylistByName(String name) {

        EntityManager entityManager = sessionFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Playlist> criteriaQuery = criteriaBuilder.createQuery(Playlist.class);
        Root<Playlist> root = criteriaQuery.from(Playlist.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Playlist_.NAME), name));

        TypedQuery<Playlist> query = entityManager.createQuery(criteriaQuery);
        Playlist result = query.getSingleResult();


        entityManager.close();
        return result;
    }
    public void createPlaylist(String name){
        Playlist playlist = new Playlist(name);

        EntityManager entityManager = sessionFactory.createEntityManager();


        entityManager.getTransaction().begin();

        entityManager.persist(playlist);

        entityManager.getTransaction().commit();
    }
}
