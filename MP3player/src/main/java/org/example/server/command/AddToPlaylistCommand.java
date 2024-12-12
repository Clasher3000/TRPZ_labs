package org.example.server.command;

import org.example.server.service.TrackService;

import java.io.PrintWriter;

public class AddToPlaylistCommand implements Command{
    private TrackService trackService;
    private String trackTitle;
    private String playlistName;
    private PrintWriter out;

    public AddToPlaylistCommand(TrackService trackService, String playlistName, String trackTitle, PrintWriter out) {
        this.trackService = trackService;
        this.playlistName = playlistName;
        this.trackTitle = trackTitle;
        this.out = out;
    }


    @Override
    public void execute() {
        if (trackTitle != null && !trackTitle.isEmpty() && playlistName != null && !playlistName.isEmpty()) {
            trackService.addTrackToPlaylist(playlistName, trackTitle);
        } else {
            out.println("Please provide both playlist name and track title to complete.");
        }
    }
}
