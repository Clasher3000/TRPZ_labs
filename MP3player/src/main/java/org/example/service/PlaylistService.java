package org.example.service;

import org.example.entity.Playlist;

public interface PlaylistService {
    Playlist findByName(String name);
}
