package org.example.server.command;

import org.example.server.MusicPlayer;
import org.example.service.PlaylistService;
import org.example.service.TrackService;

public class FindAllCommand implements Command{
    private MusicPlayer musicPlayer;

    private PlaylistService playlistService;

    private TrackService trackService;

    public FindAllCommand(MusicPlayer musicPlayer, TrackService trackService, PlaylistService playlistService) {
        this.musicPlayer = musicPlayer;
        this.trackService = trackService;
        this.playlistService = playlistService;
    }

    @Override
    public void execute() {
        musicPlayer.findAllTracks(trackService, playlistService);
    }
}
