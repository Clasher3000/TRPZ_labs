package org.example.server.visitor;

import org.example.server.service.PlaylistService;
import org.example.server.service.TrackService;

import java.util.List;

public interface Visitor {

    void findAll(Element... args);
    List<String> visitTrack(TrackService trackService);
    List<String> visitPlaylist(PlaylistService playlistService);
}
