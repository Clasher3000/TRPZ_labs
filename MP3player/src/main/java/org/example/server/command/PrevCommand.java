package org.example.server.command;

import org.example.server.MusicPlayer;

public class PrevCommand implements Command{
    private MusicPlayer musicPlayer;

    public PrevCommand(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @Override
    public void execute() {
        musicPlayer.playPreviousTrackInPlaylist();
    }
}
