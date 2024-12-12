package org.example.server.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.server.entity.Playlist;
import org.example.server.entity.Track;
import org.example.server.exception.ResourceNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public class PlaylistRepository {

    private PrintWriter out;

    private final SessionFactory sessionFactory;

    public PlaylistRepository(PrintWriter out) {

        this.out = out;
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

    private EntityManager getEntityManager() {
        return sessionFactory.createEntityManager();
    }

    public Playlist getPlaylistByName(String name) {
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<Playlist> query = entityManager.createQuery(
                    "SELECT p FROM Playlist p WHERE p.name = :name", Playlist.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Playlist", name);
        } finally {
            entityManager.close();
        }
    }

    public void createPlaylist(String name) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Playlist playlist = new Playlist(name);
            entityManager.persist(playlist);
            entityManager.getTransaction().commit();
            System.out.println("Playlist created successfully: " + name);
        } finally {
            entityManager.close();
        }
    }

    public List<Playlist> findAll() {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM Playlist p", Playlist.class).getResultList();
        } catch (Exception e) {
            System.out.println("Error retrieving playlists: " + e.getMessage());
            return List.of();
        } finally {
            entityManager.close();
        }
    }

    public void shufflePlaylistTracks(String playlistName) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Отримуємо плейлист за назвою
            TypedQuery<Playlist> query = entityManager.createQuery(
                    "SELECT p FROM Playlist p WHERE p.name = :name", Playlist.class);
            query.setParameter("name", playlistName);

            Playlist playlist = query.getSingleResult();

            if (playlist != null && playlist.getTracks() != null && !playlist.getTracks().isEmpty()) {
                List<Track> tracks = playlist.getTracks();
                Collections.shuffle(tracks);

                for (int i = 0; i < tracks.size(); i++) {
                    tracks.get(i).setPosition(i + 1);
                    entityManager.merge(tracks.get(i));
                }
               out.println("Tracks in playlist \"" + playlistName + "\" shuffled successfully.");
            } else {
                out.println("Playlist is empty");
            }
            entityManager.getTransaction().commit();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Playlist", playlistName);
        } finally {
            entityManager.close();
        }
    }

    public void deletePlaylist(String name) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            TypedQuery<Playlist> query = entityManager.createQuery(
                    "SELECT p FROM Playlist p WHERE p.name = :name", Playlist.class);
            query.setParameter("name", name);

            Playlist playlist = query.getSingleResult();
            entityManager.remove(playlist);

            out.println("Playlist with name '" + name + "' has been deleted.");

            entityManager.getTransaction().commit();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Playlist", name);
        } finally {
            entityManager.close();
        }
    }
}
