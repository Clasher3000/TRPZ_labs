package org.example.server.service;

import org.example.server.entity.Track;
import org.example.server.visitor.Element;

import java.util.List;

public interface TrackService extends Element{

    Track findByTitle(String title);

    void addTrack(String title, String path);

    void addTrackToPlaylist(String playlistName, String trackTitle);

    List<Track> findAll();

    void deleteTrack(String title);

    void deleteTrackFromPlaylist(String title);
}
