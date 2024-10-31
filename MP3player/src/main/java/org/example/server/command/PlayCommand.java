package org.example.server.command;

import org.example.server.MusicPlayer;

import java.io.PrintWriter;

public class PlayCommand implements Command {
    private MusicPlayer musicPlayer;
    private String title;
    private PrintWriter out;

    public PlayCommand(MusicPlayer musicPlayer, String title, PrintWriter out) {
        this.musicPlayer = musicPlayer;
        this.title = title;
        this.out = out;
    }

    @Override
    public void execute() {
        try {
            if (title != null && !title.isEmpty()) {
                musicPlayer.playSong(title);
                out.println("Playing song: " + title);
            } else {
                out.println("Please provide a song name.");
            }
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
