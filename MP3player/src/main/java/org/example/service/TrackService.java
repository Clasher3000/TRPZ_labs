package org.example.service;

import org.example.entity.Track;

public interface TrackService {

    Track findByTitle(String title);

    void addTrack(String title, String path);

    void addTrackToPlaylist(String playlistName, String trackTitle);
}
