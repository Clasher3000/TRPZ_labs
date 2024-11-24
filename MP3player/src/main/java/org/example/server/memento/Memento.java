package org.example.server.memento;

import org.example.entity.Playlist;
import org.example.entity.Track;

public class Memento {
    private Playlist playlist;

    private Track track;

    public Memento(Playlist playlist, Track track) {
        this.playlist = playlist;
        this.track = track;
    }
    public Memento(Track track) {
        playlist = null;
        this.track = track;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Track getTrack() {
        return track;
    }

    public boolean isPlaylistBased() {
        return playlist != null;
    }
}
