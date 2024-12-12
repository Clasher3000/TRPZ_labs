package org.example.server.service;

import org.example.server.entity.Playlist;
import org.example.server.visitor.Element;

import java.util.List;

public interface PlaylistService extends Element {

    Playlist findByName(String name);

    void create(String name);

    List<Playlist> findAll();

    void shufflePlaylistTracks(String playlistName);

    void deletePlaylist(String name);
}
