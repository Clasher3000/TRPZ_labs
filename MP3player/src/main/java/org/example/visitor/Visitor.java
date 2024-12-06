package org.example.visitor;

import org.example.entity.Track;
import org.example.service.PlayListServiceImpl;
import org.example.service.PlaylistService;
import org.example.service.TrackService;

import java.util.List;

public interface Visitor {

    void findAll(Element... args);
    List<String> visitTrack(TrackService trackService);
    List<String> visitPlaylist(PlaylistService playlistService);
}
