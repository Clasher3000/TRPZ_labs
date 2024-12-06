package org.example.server;

import jakarta.persistence.NoResultException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.example.entity.Playlist;
import org.example.entity.Track;
import org.example.server.iterator.TrackIterator;
import org.example.server.iterator.TrackIteratorImpl;
import org.example.server.memento.Caretaker;
import org.example.server.memento.Memento;
import org.example.service.PlayListServiceImpl;
import org.example.service.PlaylistService;
import org.example.service.TrackService;
import org.example.service.TrackServiceImpl;
import org.example.visitor.Element;
import org.example.visitor.FindVisitor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.List;

public class MusicPlayer {
    private TrackService trackService;
    private PlaylistService playlistService;
    private AdvancedPlayer player;
    private Thread playThread;
    private PrintWriter out;
    private Track track;
    private Playlist playlist;
    private int pausedFrame = 0;
    private boolean isPaused = false;
    private TrackIterator trackIterator;
    private final Caretaker caretaker = new Caretaker();

    public MusicPlayer(PrintWriter out) {
        this.trackService = new TrackServiceImpl();
        this.playlistService = new PlayListServiceImpl();
        this.out = out;
    }

    public void playSong(String fileName) {
        stopSong();

        playThread = new Thread(() -> {
            try {
                track = trackService.findByTitle(fileName);
                if (track == null) {
                    out.println("Track not found.");
                    return;
                }
                FileInputStream fis = new FileInputStream(track.getPath());
                BufferedInputStream bis = new BufferedInputStream(fis);

                player = new AdvancedPlayer(bis);
                out.println("Playing: " + fileName);
                player.play();

            } catch (NoResultException e) {
                out.println("Incorrect track title");
            } catch (JavaLayerException | IOException e) {
                out.println("Error playing song: " + e.getMessage());
            }
        });

        playThread.start();
    }


    public void playPlaylist(String playlistName) {
        try {
            playlist = playlistService.findByName(playlistName);
            if (playlist != null && playlist.getTracks() != null && !playlist.getTracks().isEmpty()) {
                trackIterator = new TrackIteratorImpl(playlist.getTracks());

                playNextTrackInPlaylist();
                out.println("Playlist: " + playlistName + " is playing");
            } else {
                out.println("Playlist is empty or does not exist.");
            }
        } catch (NoResultException e) {
            out.println("Incorrect playlist name");
        }
    }

    public synchronized void playNextTrackInPlaylist() {
        if (player != null) {
            stopSong();
        }

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

    public void playPlaylistOnTrack(String playlistName, String trackTitle) {
        try {
            stopSong();
            playlist = playlistService.findByName(playlistName);

            if (playlist != null && playlist.getTracks() != null && !playlist.getTracks().isEmpty()) {
                trackIterator = new TrackIteratorImpl(playlist.getTracks());


                boolean trackFound = false;
                while (trackIterator.hasNext()) {
                    Track currentTrack = trackIterator.next();
                    if (currentTrack.getTitle().equalsIgnoreCase(trackTitle)) {
                        trackFound = true;
                        playSong(currentTrack.getTitle());
                        break;
                    }
                }

                if (!trackFound) {
                    out.println("Track " + trackTitle + " not found in playlist " + playlistName + ".");
                } else {
                    out.println("Playing playlist " + playlistName + " starting from track " + trackTitle + ".");
                }
            } else {
                out.println("Playlist " + playlistName + " is empty or does not exist.");
            }
        } catch (NoResultException e) {
            out.println("Playlist " + playlistName + " not found.");
        }
    }


    public void stopSong() {
        if (player != null) {
            player.close();
            System.out.println("Song stopped.");
        }
        if (playThread != null && playThread.isAlive()) {
            playThread.interrupt();
        }
        track = null;
    }
    public void clearPlaylist() {
        playlist = null;
        trackIterator = null;
    }


    public void pauseSong() {
        if (player != null && !isPaused) {
            isPaused = true;
            player.close();
            pausedFrame = 300;
            System.out.println("Song paused at frame: " + pausedFrame);
        } else {
            System.out.println("No song is currently playing.");
        }
    }

    public void resumeSong() {
        if (isPaused) {
            isPaused = false;

            playSong(track.getTitle());
        } else {
            out.println("No song is currently paused.");
        }
    }

    public void saveState() {
        if (track != null) {
            if (playlist != null){
                caretaker.save(playlist, track);
                out.println("State saved: Playlist - " + playlist.getName() + ", Track - " + track.getTitle());
            }
            else{
                caretaker.save(track);
                out.println("State saved: Track - " + track.getTitle());
            }
        }
        else {
            out.println("Nothing is playing now.");
        }

    }


    public void restoreState() {
        Memento memento = caretaker.undo();
        if (memento != null) {
            // Отримуємо трек і плейлист зі збереженого стану
            Playlist savedPlaylist = memento.getPlaylist();
            Track savedTrack = memento.getTrack();

            if (savedPlaylist != null && savedTrack != null) {
                // Відновлюємо плейлист і починаємо збережений трек
                playPlaylistOnTrack(savedPlaylist.getName(), savedTrack.getTitle());
            } else if (savedTrack != null) {
                // Якщо є тільки трек, граємо його
                playSong(savedTrack.getTitle());
            } else {
                out.println("No saved track or playlist to restore.");
            }
        } else {
            out.println("No saved state available.");
        }
    }

    public void findAllTracks(Element... args){
        FindVisitor findVisitor = new FindVisitor(out);
        findVisitor.findAll(args);
    }
}
