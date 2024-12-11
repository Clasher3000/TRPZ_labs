package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.Playlist;
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

    public void saveTrack(String title, String path){
            Track track = new Track(title, path);

            EntityManager entityManager = sessionFactory.createEntityManager();


            entityManager.getTransaction().begin();

            entityManager.persist(track);

            entityManager.getTransaction().commit();
    }

    public void addTrackToPlaylist(Track track, Playlist playlist) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

        if (playlist != null && track != null) {
            // Спочатку перевіряємо, чи є треки в плейлисті
            List<Track> tracks = entityManager.createQuery("SELECT t FROM Track t WHERE t.playlist = :playlist", Track.class)
                    .setParameter("playlist", playlist)
                    .getResultList();

            int newPosition;
            if (!tracks.isEmpty()) {
                // Якщо треки є, знаходимо останній і присвоюємо новій пісні позицію
                Track lastTrack = tracks.get(tracks.size() - 1);
                newPosition = lastTrack.getPosition() + 1;
            } else {
                // Якщо немає треків, присвоюємо позицію 1
                newPosition = 1;
            }

            track.setPlaylist(playlist);
            track.setPosition(newPosition);  // Встановлюємо нову позицію

            // Оновлюємо або зберігаємо трек
            entityManager.merge(track);
            System.out.println("Track added with position: " + newPosition);
        } else {
            System.out.println("Playlist or Track not found.");
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }


    public List<Track> findAllTracks() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Track> tracks;
        try {
            // Використання JPQL для отримання всіх треків
            tracks = entityManager.createQuery("SELECT t FROM Track t", Track.class).getResultList();
            if (tracks.isEmpty()) {
                System.out.println("No tracks found.");
            } else {
                System.out.println("Tracks retrieved successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving tracks: " + e.getMessage());
            tracks = List.of(); // Повертаємо порожній список у разі помилки
        }

        entityManager.getTransaction().commit();
        entityManager.close();

        return tracks;
    }


    public void deleteTrack(String title) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

            // Find track by title
            Track track = entityManager.createQuery("SELECT t FROM Track t WHERE t.title = :title", Track.class)
                    .setParameter("title", title)
                    .getSingleResult();

            if (track != null) {
                // If the track is associated with a playlist, disassociate it
                if (track.getPlaylist() != null) {
                    track.setPlaylist(null); // Break the association with the playlist
                }

                // Now safely delete the track
                entityManager.remove(track);
                System.out.println("Track with title '" + title + "' has been deleted.");
            } else {
                System.out.println("Track with title '" + title + "' not found.");
            }

        entityManager.getTransaction().commit();
        entityManager.close();
    }


}
