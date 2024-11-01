package org.example.server.iterator;

import org.example.entity.Track;

public interface TrackIterator {
    boolean hasNext();
    Track next();
    boolean hasPrevious();
    Track previous();

}
