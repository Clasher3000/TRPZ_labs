package org.example.server.iterator;

import org.example.server.entity.Playlist;

public interface PlaylistIterator {
    boolean hasNext();
    Playlist next();
}
