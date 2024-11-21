package org.example.server.command;

import org.example.server.MusicPlayer;
import org.example.server.command.Command;

import java.io.PrintWriter;

public class StartCommand implements Command {

    private MusicPlayer musicPlayer;
    private PrintWriter out;

    private String name;
    public StartCommand(MusicPlayer musicPlayer, String argument, PrintWriter out) {
        this.musicPlayer=musicPlayer;
        this.name = argument;
        this.out = out;
    }

    @Override
    public void execute() {
        if (name != null && !name.isEmpty()) {
            musicPlayer.playPlaylist(name);
        }
        else out.println("Please provide a playlist name");
    }
}
