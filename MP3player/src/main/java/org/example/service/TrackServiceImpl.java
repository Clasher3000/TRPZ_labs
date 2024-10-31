package org.example.service;

import org.example.entity.Track;
import org.example.repository.TrackRepository;

public class TrackServiceImpl implements TrackService {

    private TrackRepository trackRepository;

    public TrackServiceImpl() {
        this.trackRepository = new TrackRepository();
    }

    @Override
    public Track findByTitle(String title) {
        return trackRepository.getTrackByTitle(title);

    }

    @Override
    public void addTrack(String title, String path) {
        trackRepository.saveTrack(title, path);
    }
}
