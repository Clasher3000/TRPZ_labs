package org.example.server.command;

import org.example.server.MusicPlayer;

import java.io.PrintWriter;

public class AddTrackCommand implements Command {
    private MusicPlayer musicPlayer;
    private String title;
    private String filePath;
    private PrintWriter out;

    public AddTrackCommand(MusicPlayer musicPlayer, String title, String filePath, PrintWriter out) {
        this.musicPlayer = musicPlayer;
        this.title = title;
        this.filePath = filePath;
        this.out = out;
    }


    @Override
    public void execute() {
        if (title != null && !title.isEmpty() && filePath != null && !filePath.isEmpty()) {
            musicPlayer.addTrack(title, filePath);
            out.println("Track added: " + title);
        } else {
            out.println("Please provide both title and file path for the track.");
        }
    }
}
