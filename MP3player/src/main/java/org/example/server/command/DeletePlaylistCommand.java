package org.example.server.command;

import org.example.server.service.PlaylistService;

import java.io.PrintWriter;

public class DeletePlaylistCommand implements Command{

    private PrintWriter out;
    private PlaylistService playlistService;
    private String name;

    public DeletePlaylistCommand(PlaylistService playlistService, String name, PrintWriter out) {
        this.playlistService = playlistService;
        this.name = name;
        this.out = out;
    }

    @Override
    public void execute() {
        playlistService.deletePlaylist(name);
    }
}
