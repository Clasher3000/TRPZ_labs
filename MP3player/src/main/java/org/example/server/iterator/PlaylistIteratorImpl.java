package org.example.server.iterator;

import org.example.entity.Playlist;
import org.example.entity.Track;

import java.util.List;

public class PlaylistIteratorImpl implements PlaylistIterator{
    private List<Playlist> playlists;
    private int position = 0;

    public PlaylistIteratorImpl(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public boolean hasNext() {
        return position < playlists.size();
    }

    @Override
    public Playlist next() {
        return hasNext() ? playlists.get(position++) : null;
    }

}
