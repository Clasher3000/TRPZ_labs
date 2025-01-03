package org.example.server.command;

import java.io.PrintWriter;

public class HelpCommand implements Command {

    private PrintWriter out;

    public HelpCommand(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void execute() {
        out.println("Commands:");
        out.println("play <track_title | track_url> - Play a specific track.");
        out.println("radio <?radio_name> - Play a radio. Radio names: KEXP, HitFM, Rocks, ClassicFM, Paradise.");
        out.println("stop - Stop the current track.");
        out.println("pause - Pause the current track.");
        out.println("resume - Resume the current track.");
        out.println("start <playlist_name> - Play a specific playlist.");
        out.println("next  - Play next track in playlist.");
        out.println("add <track_title> <track path> - Add track.");
        out.println("create_playlist <playlist_name> - Create playlist.");
        out.println("add_to_playlist <playlist_name>,<track_title> - Add track to the playlist.");
        out.println("save_state - Save current state.");
        out.println("restore_state - Restore previous state.");
        out.println("shuffle <playlist_name> - Shuffle the tracks in the playlist.");
        out.println("repeat  - Turn on/off repeat.");
        out.println("delete_track <track_name> - Delete track from the system.");
        out.println("delete_playlist <playlist_name> - Delete playlist.");
        out.println("exit - Disconnect from the server.");
    }
}