package org.example.server.command;

import org.example.server.MusicPlayer;

public class RepeatCommand implements Command{

    MusicPlayer musicPlayer;

    public RepeatCommand(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @Override
    public void execute() {
        musicPlayer.toggleRepeat();
    }
}
