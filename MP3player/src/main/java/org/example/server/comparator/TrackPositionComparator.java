package org.example.server.comparator;

import org.example.entity.Track;

import java.util.Comparator;

public class TrackPositionComparator implements Comparator<Track> {

    @Override
    public int compare(Track track1, Track track2) {
        return Integer.compare(track1.getPosition(), track2.getPosition());
    }
}
