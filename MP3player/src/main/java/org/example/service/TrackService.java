package org.example.service;

import org.example.entity.Track;
import org.example.visitor.Element;

import java.util.List;

public interface TrackService extends Element{

    Track findByTitle(String title);

    void addTrack(String title, String path);

    void addTrackToPlaylist(String playlistName, String trackTitle);

    List<Track> findAll();

    void deleteTrack(String title);
}
