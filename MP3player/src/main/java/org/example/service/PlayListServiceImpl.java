package org.example.service;

import org.example.entity.Playlist;
import org.example.repository.PlaylistRepository;

public class PlayListServiceImpl implements PlaylistService{

    private PlaylistRepository playlistRepository;

    public PlayListServiceImpl() {
        this.playlistRepository = new PlaylistRepository();
    }

    @Override
    public Playlist findByName(String name) {
        return playlistRepository.getPlaylistByName(name);
    }

    @Override
    public void create(String name) {
        playlistRepository.createPlaylist(name);
    }
}
