package org.example.server.command;

import org.example.server.MusicPlayer;
import org.example.service.PlaylistService;

import java.io.PrintWriter;

public class CreatePlaylistCommand implements Command{
    private PlaylistService playlistService;
    private String name;
    private PrintWriter out;

    public CreatePlaylistCommand(PlaylistService playlistService, String name, PrintWriter out) {
        this.playlistService = playlistService;
        this.name = name;
        this.out = out;
    }


    @Override
    public void execute() {
        if (name != null && !name.isEmpty()) {
            playlistService.create(name);
        } else {
            out.println("Please provide both title and file path for the track.");
        }
    }
}
