package org.example.server.command;

import org.example.server.service.TrackService;

public class DeleteTrackFromPlaylistCommand implements Command{

    private TrackService trackService;
    private String title;

    public DeleteTrackFromPlaylistCommand(TrackService trackService, String title) {
        this.trackService = trackService;
        this.title = title;
    }

    public void execute() {
        trackService.deleteTrackFromPlaylist(title);
    }

}
