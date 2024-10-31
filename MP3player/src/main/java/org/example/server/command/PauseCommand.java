package org.example.server.command;

import org.example.server.MusicPlayer;

import java.io.IOException;
import java.io.PrintWriter;

public class PauseCommand implements Command{

    private MusicPlayer musicPlayer;
    private PrintWriter out;
    public PauseCommand(MusicPlayer musicPlayer, PrintWriter out) {
        this.musicPlayer = musicPlayer;
        this.out = out;

    }

    @Override
    public void execute() {
        musicPlayer.pauseSong();
    }
}
