package org.example.server.command;

import org.example.service.TrackService;

import java.io.PrintWriter;

public class DeleteTrackCommand implements Command {

    private PrintWriter out;
    private TrackService trackService;
    private String title;

    public DeleteTrackCommand(TrackService trackService, String title, PrintWriter out) {
        this.trackService = trackService;
        this.title = title;
        this.out = out;
    }

    @Override
    public void execute() {
        trackService.deleteTrack(title);
    }
}
