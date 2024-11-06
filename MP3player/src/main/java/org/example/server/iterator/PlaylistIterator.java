package org.example.server.iterator;

import org.example.entity.Playlist;
import org.example.entity.Track;

public interface PlaylistIterator {
    boolean hasNext();
    Playlist next();
}
