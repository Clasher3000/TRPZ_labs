package org.example.server;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.example.entity.Playlist;
import org.example.entity.Track;
import org.example.service.PlayListServiceImpl;
import org.example.service.PlaylistService;
import org.example.service.TrackService;
import org.example.service.TrackServiceImpl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

    public MusicPlayer(PrintWriter out) {
        this.trackService = new TrackServiceImpl();
        this.playlistService = new PlayListServiceImpl();
        this.out = out;
    }

    public void playSong(String fileName) {
        stopSong(); // Зупиняємо попередню пісню, якщо вона відтворюється

        playThread = new Thread(() -> {
            try {
                track = trackService.findByTitle(fileName);
                String filePath = track.getPath();
                FileInputStream fis = new FileInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(fis);

                player = new AdvancedPlayer(bis);

                System.out.println(bis);
                System.out.println("Playing: " + fileName);
                player.play(pausedFrame, Integer.MAX_VALUE); // Відтворення з кадру pausedFrame
                pausedFrame = 0; // Обнуляємо після завершення
            } catch (JavaLayerException | IOException e) {
                out.println("Error playing song: " + e.getMessage());
            }
        });

        playThread.start(); // Запускаємо відтворення в окремому потоці
    }
    public void playPlaylist(String name) {
        this.playlist = playlistService.findByName(name);
        if (playlist.getTracks()!=null) {
            currentTrackIndex = 0;
            Track tempTrack = playlist.getTracks().get(currentTrackIndex);
            playSong(tempTrack.getTitle()); // Відтворення першого треку
        } else {
            System.out.println("Playlist is empty.");
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

    public void addTrack(String title, String filePath) {
        trackService.addTrack(title, filePath); // Додаємо трек у сховище
        System.out.println("Track added: " + title + " (" + filePath + ")");
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
