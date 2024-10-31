package org.example.server.command;

import java.io.PrintWriter;

public class HelpCommand implements Command{

    private PrintWriter out;
    public HelpCommand(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void execute() {
        out.println("Commands:");
        out.println("play <track_title> - Play a specific track.");
        out.println("stop - Stop the current track.");
        out.println("add <track_title> <track path> - Add track.");
        out.println("exit - Disconnect from the server.");
    }
}
