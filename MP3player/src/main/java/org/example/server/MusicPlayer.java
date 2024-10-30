package org.example.server;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.example.repository.TrackRepository;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class MusicPlayer {

private TrackRepository trackRepository;

    public MusicPlayer() {
        this.trackRepository = new TrackRepository();
    }

    public void playSong(String fileName) {
        try {

            String filePath = trackRepository.getTrackByTitle(fileName).getPath();
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);

            try {
                // Створюємо плеєр для відтворення
                Player player = new Player(bis);
                System.out.println("Playing: " + filePath);
                player.play(); // Відтворюємо файл
            } catch (JavaLayerException e) {
                System.out.println("Error playing song: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error: File not found or cannot be opened: " + e.getMessage());
        }
    }

    public void stopSong() {
        // Це місце для реалізації зупинки, якщо ви будете використовувати інший підхід для асинхронного відтворення
        System.out.println("Stop method is not implemented.");
    }
}
