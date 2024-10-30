package org.example;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for simple App.
 */
public class HibernateFullTest {

    private EntityManagerFactory sessionFactory;

    @BeforeEach
    protected void setUp() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    @AfterEach
    protected void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @Test
    void postgresFetchTracks() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

            List<Track> tracks = entityManager.createQuery("SELECT t from Track t", Track.class).getResultList();

            tracks.forEach(System.out::println);
        entityManager.getTransaction().commit();
        }


    @Test
    void saveMyTrackToDb() {
        Track track = new Track("test", "test", 555);

        EntityManager entityManager = sessionFactory.createEntityManager();


        entityManager.getTransaction().begin();

        entityManager.persist(track);

        entityManager.getTransaction().commit();
        }
    @Test
    void criteriaApi() {

        EntityManager entityManager = sessionFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Track> criteriaQuery = criteriaBuilder.createQuery(Track.class);
        Root<Track> root = criteriaQuery.from(Track.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Track_.TITLE),"Get Lucky"));

        TypedQuery<Track> query = entityManager.createQuery(criteriaQuery);
        List<Track> results = query.getResultList();
        results.forEach(System.out::println);

        entityManager.close();
    }





}