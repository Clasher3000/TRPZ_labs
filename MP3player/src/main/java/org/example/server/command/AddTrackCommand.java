package org.example.server.command;

import org.example.server.service.TrackService;

import java.io.PrintWriter;

public class AddTrackCommand implements Command {
    private TrackService trackService;
    private String title;
    private String filePath;
    private PrintWriter out;

    public AddTrackCommand(TrackService trackService, String title, String filePath, PrintWriter out) {
        this.trackService = trackService;
        this.title = title;
        this.filePath = filePath;
        this.out = out;
    }


    @Override
    public void execute() {
        if (title != null && !title.isEmpty() && filePath != null && !filePath.isEmpty()) {
            trackService.addTrack(title, filePath); // Додаємо трек у сховище
        } else {
            out.println("Please provide both title and file path for the track.");
        }
    }
}
