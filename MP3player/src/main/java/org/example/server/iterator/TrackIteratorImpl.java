package org.example.server.iterator;

import org.example.server.entity.Track;

import java.util.List;

public class TrackIteratorImpl implements TrackIterator {
    private List<Track> tracks;
    private int position = 0;

    public TrackIteratorImpl(List<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean hasNext() {
        return position < tracks.size();
    }

    @Override
    public Track next() {
        return hasNext() ? tracks.get(position++) : null;
    }
    @Override
    public boolean hasPrevious() {
        return position > 0;
    }

    @Override
    public Track previous() {

        if(hasPrevious()){
            position = position - 1;
            Track track = tracks.get(position);
            return track;
        }
        else return null;

    }

}
