package org.example.server;

import jakarta.persistence.NoResultException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.example.entity.Playlist;
import org.example.entity.Track;
import org.example.server.iterator.TrackIterator;
import org.example.server.iterator.TrackIteratorImpl;
import org.example.service.PlayListServiceImpl;
import org.example.service.PlaylistService;
import org.example.service.TrackService;
import org.example.service.TrackServiceImpl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class MusicPlayer {
    private TrackService trackService;
    private PlaylistService playlistService;
    private AdvancedPlayer player;
    private Thread playThread;
    private PrintWriter out;

    private int currentTrackIndex = 0;
    private Track track;
    private Playlist playlist;
    private int pausedFrame = 0; // Кадр, на якому була пауза
    private boolean isPaused = false;
    private TrackIterator trackIterator;

    public MusicPlayer(PrintWriter out) {
        this.trackService = new TrackServiceImpl();
        this.playlistService = new PlayListServiceImpl();
        this.out = out;
    }

    public void playSong(String fileName) {
        stopSong();

        playThread = new Thread(() -> {
            try {
                Track track = trackService.findByTitle(fileName);
                if (track == null) {
                    out.println("Track not found.");
                    return;
                }
                FileInputStream fis = new FileInputStream(track.getPath());
                BufferedInputStream bis = new BufferedInputStream(fis);

                player = new AdvancedPlayer(bis);
                out.println("Playing: " + fileName);
                player.play();

                if(playlist!=null) {
                    playNextTrackInPlaylist();
                }
            }
            catch (NoResultException e){
                out.println("Incorrect track title");
            }
            catch (JavaLayerException | IOException e) {
                out.println("Error playing song: " + e.getMessage());
            }
        });

        playThread.start(); // Запускаємо відтворення в окремому потоці
    }
    public void playPlaylist(String playlistName) {
        try {
            Playlist playlist = playlistService.findByName(playlistName);
            if (playlist != null && playlist.getTracks() != null && !playlist.getTracks().isEmpty()) {
                trackIterator = new TrackIteratorImpl(playlist.getTracks());
                playNextTrackInPlaylist();
                out.println("Playlist: " + playlistName + " is playing");
            } else {
                out.println("Playlist is empty or does not exist.");
            }
        } catch (NoResultException e){
            out.println("Incorrect playlist name");
        }
    }
    public void playNextTrackInPlaylist() {
        if (trackIterator != null && trackIterator.hasNext()) {
            Track nextTrack = trackIterator.next();
            playSong(nextTrack.getTitle());
        } else {
            out.println("There are no more tracks.");
        }
    }
    public void playPreviousTrackInPlaylist() {
        if (trackIterator != null && trackIterator.hasPrevious()) {
            Track previousTrack = trackIterator.previous();
            playSong(previousTrack.getTitle());
        } else {
            out.println("There are no tracks before.");
        }
    }

    public void stopSong() {
        if (player != null) {
            player.close(); // Зупиняємо відтворення
            System.out.println("Song stopped.");

        }
        if (playThread != null && playThread.isAlive()) {
            playThread.interrupt(); // Перериваємо потік, якщо він активний
        }
        pausedFrame = 0; // Скидаємо кадр паузи
        track = null;
        playlist = null;
        currentTrackIndex = 0;
    }


    public void pauseSong() {
        if (player != null && !isPaused) {
            isPaused = true; // Ставимо стан паузи
            player.close(); // Зупиняємо відтворення
            pausedFrame = 300;
            System.out.println("Song paused at frame: " + pausedFrame);
        } else {
            System.out.println("No song is currently playing.");
        }
    }

    public void resumeSong() {
        if (isPaused) {
            isPaused = false; // Скидаємо стан призупинення

            playSong(track.getTitle()); // Відновлюємо відтворення
        } else {
            out.println("No song is currently paused.");
        }
    }
}
