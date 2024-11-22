package org.example.server.command;

import org.example.server.MusicPlayer;

public class RestoreStateCommand implements Command{

    private MusicPlayer musicPlayer;

    public RestoreStateCommand(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @Override
    public void execute() {
        musicPlayer.restoreState();
    }
}
