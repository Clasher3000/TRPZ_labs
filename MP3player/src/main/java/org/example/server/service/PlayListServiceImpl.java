package org.example.server.service;

import jakarta.persistence.NoResultException;
import org.example.server.entity.Playlist;
import org.example.server.repository.PlaylistRepository;
import org.example.server.exception.ResourceAlreadyExistsException;
import org.example.server.exception.ResourceNotFoundException;
import org.example.server.visitor.Visitor;

import java.io.PrintWriter;
import java.util.List;

public class PlayListServiceImpl implements PlaylistService {

    private PrintWriter out;

    @Override
    public List<String> accept(Visitor visitor) {
        return visitor.visitPlaylist(this);
    }

    private PlaylistRepository playlistRepository;

    public PlayListServiceImpl(PrintWriter out) {
        this.playlistRepository = new PlaylistRepository(out);
        this.out = out;
    }

    @Override
    public Playlist findByName(String name) {
            return playlistRepository.getPlaylistByName(name);
        }


    @Override
    public void create(String name) {
        try{
        if(doesPlaylistExist(name)) {
            throw new ResourceAlreadyExistsException("Playlist", name);
        }
        }catch (ResourceNotFoundException e){
        playlistRepository.createPlaylist(name);
        out.println("Playlist created: " + name);
        }
    }

    @Override
    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }


    @Override
    public void shufflePlaylistTracks(String playlistName) {
            playlistRepository.shufflePlaylistTracks(playlistName);
        }

    @Override
    public void deletePlaylist(String name) {
        playlistRepository.deletePlaylist(name);
    }

    private boolean doesPlaylistExist(String playlistName) {
        try {
            Playlist playlist = playlistRepository.getPlaylistByName(playlistName);
            return playlist != null;  // Якщо трек знайдено, повертаємо true
        } catch (NoResultException e) {
            return false;  // Якщо трек не знайдено, повертаємо false
        }
    }
    }



