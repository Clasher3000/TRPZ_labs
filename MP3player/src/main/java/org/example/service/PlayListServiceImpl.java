package org.example.service;

import org.example.entity.Playlist;
import org.example.repository.PlaylistRepository;
import org.example.visitor.Element;
import org.example.visitor.Visitor;

import java.util.List;

public class PlayListServiceImpl implements PlaylistService {

    @Override
    public List<String> accept(Visitor visitor) {
        return visitor.visitPlaylist(this);
    }

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

    @Override
    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }
}
