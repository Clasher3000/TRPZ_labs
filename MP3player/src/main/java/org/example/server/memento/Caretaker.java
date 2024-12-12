package org.example.server.memento;

import org.example.server.entity.Playlist;
import org.example.server.entity.Track;
import org.example.server.MusicPlayer;

import java.util.Stack;

public class Caretaker {

    private MusicPlayer musicPlayer;
    private final Stack<Memento> mementoStack = new Stack<>();


    public void save(Playlist playlist, Track track) {
        mementoStack.push(new Memento(playlist, track));
        System.out.println("State saved: Playlist - " +
                (playlist != null ? playlist.getName() : "null") +
                ", Track - " + track.getTitle());
    }

    public void save(Track track) {
        mementoStack.push(new Memento(track));
        System.out.println("State saved: Individual Track - " + track.getTitle());
    }

    public Memento undo() {
        if (!mementoStack.isEmpty()) {
            Memento previousState = mementoStack.pop();
            if (previousState.isPlaylistBased()) {
                System.out.println("State restored: Playlist - " +
                        previousState.getPlaylist().getName() +
                        ", Track - " + previousState.getTrack().getTitle());
            } else {
                System.out.println("State restored: Individual Track - " +
                        previousState.getTrack().getTitle());
            }
            return previousState;
        }
        System.out.println("No states to restore.");
        return null;
    }



}
