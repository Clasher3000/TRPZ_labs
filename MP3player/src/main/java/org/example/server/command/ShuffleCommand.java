package org.example.server.command;

import org.example.server.MusicPlayer;

public class ShuffleCommand implements Command{

    private MusicPlayer musicPlayer;
    private String name;

    public ShuffleCommand(MusicPlayer musicPlayer, String name) {
        this.musicPlayer = musicPlayer;
        this.name = name;
    }

    @Override
    public void execute() {
            musicPlayer.shufflePlaylist(name);
    }
}
