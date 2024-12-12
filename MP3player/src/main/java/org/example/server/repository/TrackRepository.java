package org.example.server.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.server.entity.Playlist;
import org.example.server.entity.Track;
import org.example.entity.Track_;
import org.example.server.exception.ResourceNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.PrintWriter;
import java.util.List;

public class TrackRepository {

    private final SessionFactory sessionFactory;
    private final PrintWriter out;

    public TrackRepository(PrintWriter out) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Не вдалося створити SessionFactory", e);
        }
        this.out = out;
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private EntityManager getEntityManager() {
        return sessionFactory.createEntityManager();
    }

    public List<Track> getAll() {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT t FROM Track t", Track.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Track getTrackByTitle(String title) {
        EntityManager entityManager = getEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Track> criteriaQuery = criteriaBuilder.createQuery(Track.class);
            Root<Track> root = criteriaQuery.from(Track.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Track_.TITLE), title));

            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Track", title);
        } finally {
            entityManager.close();
        }
    }

    public void saveTrack(String title, String path) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Track track = new Track(title, path);
            entityManager.persist(track);
            entityManager.getTransaction().commit();
            out.println("Track saved successfully: " + title);
        } finally {
            entityManager.close();
        }
    }

    public void addTrackToPlaylist(Track track, Playlist playlist) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            if (track != null && playlist != null) {
                int newPosition = getLastTrackPosition(entityManager, playlist) + 1;
                track.setPlaylist(playlist);
                track.setPosition(newPosition);
                entityManager.merge(track);
                out.println("Track \"" + track.getTitle() + "\" added to playlist \"" + playlist.getName() + "\" at position " + newPosition);
            } else {
                out.println("Track or playlist not found.");
            }

            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    private int getLastTrackPosition(EntityManager entityManager, Playlist playlist) {
        List<Track> tracks = entityManager.createQuery(
                        "SELECT t FROM Track t WHERE t.playlist = :playlist ORDER BY t.position DESC", Track.class)
                .setParameter("playlist", playlist)
                .getResultList();

        return tracks.isEmpty() ? 0 : tracks.get(0).getPosition();
    }

    public void deleteTrack(String title) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            Track track = entityManager.createQuery("SELECT t FROM Track t WHERE t.title = :title", Track.class)
                    .setParameter("title", title)
                    .getSingleResult();

            if (track != null) {
                track.setPlaylist(null);
                entityManager.remove(track);
                out.println("Track with title \"" + title + "\" has been deleted.");
            } else {
                out.println("Track with title \"" + title + "\" not found.");
            }

            entityManager.getTransaction().commit();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Track", title);
        } finally {
            entityManager.close();
        }
    }

    public void deleteTrackFromPlaylist(String title) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            Track track = entityManager.createQuery("SELECT t FROM Track t WHERE t.title = :title", Track.class)
                    .setParameter("title", title)
                    .getSingleResult();

            if (track != null && track.getPlaylist() != null) {
                track.setPlaylist(null);
                entityManager.merge(track);
                out.println("Track \"" + title + "\" removed from playlist.");
            } else if (track != null) {
                out.println("Track \"" + title + "\" is not associated with any playlist.");
            }

            entityManager.getTransaction().commit();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Track", title);
        } finally {
            entityManager.close();
        }
    }
}
