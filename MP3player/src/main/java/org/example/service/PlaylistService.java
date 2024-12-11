package org.example.service;

import org.example.entity.Playlist;
import org.example.visitor.Element;

import java.util.List;

public interface PlaylistService extends Element {

    Playlist findByName(String name);

    void create(String name);

    List<Playlist> findAll();

    void shufflePlaylistTracks(String playlistName);

    void deletePlaylist(String name);
}
