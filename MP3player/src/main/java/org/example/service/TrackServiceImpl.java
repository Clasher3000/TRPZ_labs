package org.example.service;

import org.example.entity.Playlist;
import org.example.entity.Track;
import org.example.repository.TrackRepository;
import org.example.visitor.Element;
import org.example.visitor.Visitor;

import java.util.List;

public class TrackServiceImpl implements TrackService {

    private TrackRepository trackRepository;
    private PlaylistService playlistService;


    @Override
    public List<String> accept(Visitor visitor) {
        return visitor.visitTrack(this);
    }


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

    public List<Track> findAll() {
        return trackRepository.findAllTracks();
    }

}
