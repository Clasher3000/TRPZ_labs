package org.example.server.iterator;

import org.example.server.entity.Track;

public interface TrackIterator{
    boolean hasNext();
    Track next();
    boolean hasPrevious();
    Track previous();

}
