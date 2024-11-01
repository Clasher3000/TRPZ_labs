package org.example.service;

import org.example.entity.Playlist;
import org.example.entity.Track;
import org.example.repository.TrackRepository;

public class TrackServiceImpl implements TrackService {

    private TrackRepository trackRepository;
    private PlaylistService playlistService;

    public TrackServiceImpl() {
        this.playlistService = new PlayListServiceImpl();
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
    public void addTrackToPlaylist(String playlistName, String trackTitle) {
            Track track = trackRepository.getTrackByTitle(trackTitle);
            Playlist playlist = playlistService.findByName(playlistName);
            trackRepository.addTrackToPlaylist(track,playlist);

    }

}
