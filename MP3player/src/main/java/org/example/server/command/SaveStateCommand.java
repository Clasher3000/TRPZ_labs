package org.example.server.command;

import org.example.server.MusicPlayer;

public class SaveStateCommand implements Command{

    private MusicPlayer musicPlayer;

    public SaveStateCommand(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @Override
    public void execute() {
        musicPlayer.saveState();
    }
}
