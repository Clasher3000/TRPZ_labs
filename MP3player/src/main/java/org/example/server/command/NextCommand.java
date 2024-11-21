package org.example.server.command;

import org.example.server.MusicPlayer;

import java.io.PrintWriter;

public class NextCommand implements Command{

    private MusicPlayer musicPlayer;
    private PrintWriter out;

    public NextCommand(MusicPlayer musicPlayer, PrintWriter out) {
        this.musicPlayer = musicPlayer;
        this.out = out;
    }

    @Override
    public void execute() {
        musicPlayer.playNextTrackInPlaylist();
    }
}
