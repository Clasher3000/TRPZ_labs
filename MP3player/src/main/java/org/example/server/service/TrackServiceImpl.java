package org.example.server.service;

import jakarta.persistence.NoResultException;
import org.example.server.entity.Playlist;
import org.example.server.entity.Track;
import org.example.server.repository.TrackRepository;
import org.example.server.exception.ResourceNotFoundException;
import org.example.server.visitor.Visitor;

import java.io.PrintWriter;
import java.util.List;

public class TrackServiceImpl implements TrackService {

    private TrackRepository trackRepository;
    private PlaylistService playlistService;
    private PrintWriter out;

    @Override
    public List<String> accept(Visitor visitor) {
        return visitor.visitTrack(this);
    }

    public TrackServiceImpl(PrintWriter out) {
        this.playlistService = new PlayListServiceImpl(out);
        this.trackRepository = new TrackRepository(out);
        this.out = out;
    }

    @Override
    public Track findByTitle(String title) {
        try {
            return trackRepository.getTrackByTitle(title);
        } catch (NoResultException e) {
            System.out.println("Track " + title + "not found" );
            throw new ResourceNotFoundException("Track", title);
        }
    }

    @Override
    public void addTrack(String title, String path) {
        trackRepository.saveTrack(title, path);
    }
    public void addTrackToPlaylist(String playlistName, String trackTitle) {
        try {
            Track track = trackRepository.getTrackByTitle(trackTitle);
            Playlist playlist = playlistService.findByName(playlistName);
            trackRepository.addTrackToPlaylist(track, playlist);
        }
        catch (NoResultException e){
            throw new ResourceNotFoundException("Track or Playlist", playlistName + "|"+  trackTitle);
        }
    }

    public List<Track> findAll() {
        return trackRepository.getAll();
    }

    @Override
    public void deleteTrack(String title) {
        if (!doesTrackExist(title)) {
            throw new ResourceNotFoundException("Track", title);
        }
        trackRepository.deleteTrack(title);
        out.println("Track deleted: " + title);
    }

    @Override
    public void deleteTrackFromPlaylist(String title) {
        trackRepository.deleteTrackFromPlaylist(title);
    }

    private boolean doesTrackExist(String trackTitle) {
        try {
            Track track = trackRepository.getTrackByTitle(trackTitle);
            return track != null;
        } catch (NoResultException e) {
            return false;
        }
    }

}
