package org.example.server.command;

import org.example.server.MusicPlayer;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ExitCommand implements Command{

    private Socket s;
    private ServerSocket ss;
    private MusicPlayer musicPlayer;
    private PrintWriter out;

    public ExitCommand(Socket s, ServerSocket ss, MusicPlayer musicPlayer, PrintWriter out) {
        this.s = s;
        this.ss = ss;
        this.musicPlayer = musicPlayer;
        this.out = out;
    }

    @Override
    public void execute() {
        try {
            out.println("Goodbye!");
            s.close();
            ss.close();
            musicPlayer.stopSong();
        }
        catch (Exception e){
            out.print("Error: " + e.getMessage());
        }
    }
}
