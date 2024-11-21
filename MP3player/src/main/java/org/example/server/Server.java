package org.example.server;

import org.example.server.command.*;
import org.example.service.PlayListServiceImpl;
import org.example.service.PlaylistService;
import org.example.service.TrackService;
import org.example.service.TrackServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static MusicPlayer musicPlayer;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4999);
        System.out.println("Server is waiting for connection...");

        Socket s = ss.accept();
        System.out.println("Client connected.");


        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        musicPlayer = new MusicPlayer(out);
        PlaylistService playlistService = new PlayListServiceImpl();
        TrackService trackService = new TrackServiceImpl();

        String clientMessage;
        while ((clientMessage = in.readLine()) != null) {
            System.out.println("Received from client: " + clientMessage);

            String[] parts = clientMessage.split(" ", 2); // Розділити команду та аргументи
            String command = parts[0].toLowerCase();
            String myArgs = parts.length > 1 ? parts[1] : "";

            // Розділяємо аргументи за комами
            String[] arguments = myArgs.split(","); // args[0] - назва треку, args[1] - шлях до файлу

            switch (command) {
                case "help":
                    Command helpCommand = new HelpCommand(out);
                    helpCommand.execute();
                    break;
                case "play":
                    Command playCommand = new PlayCommand(musicPlayer, arguments[0], out);
                    playCommand.execute();
                    break;
                case "pause":
                    Command pauseCommand = new PauseCommand(musicPlayer, out);
                    pauseCommand.execute();
                    break;
                case "resume":
                    Command resumeCommand = new ResumeCommand(musicPlayer, out);
                    resumeCommand.execute();
                    break;
                case "stop":
                    Command stopCommand = new StopCommand(musicPlayer, out);
                    stopCommand.execute();
                    break;
                case "start":
                    Command startCommand = new StartCommand(musicPlayer,arguments[0], out);
                    startCommand.execute();
                    break;
                case "next":
                    Command nextCommand = new NextCommand(musicPlayer,out);
                    nextCommand.execute();
                    break;
                case "prev":
                    Command prevCommand = new PrevCommand(musicPlayer);
                    prevCommand.execute();
                    break;
                case "add":
                    Command addTrackCommand = new AddTrackCommand(trackService, arguments[0], arguments[1], out);
                    addTrackCommand.execute();
                    break;
                case "create_playlist":
                    Command createPlaylist = new CreatePlaylistCommand(playlistService, arguments[0], out);
                    createPlaylist.execute();
                    break;
                case "add_to_playlist":
                    Command addToPlaylist = new AddToPlaylistCommand(trackService, arguments[0], arguments[1], out);
                    addToPlaylist.execute();
                    break;
                case "exit":
                    out.println("Goodbye!");
                    s.close();
                    ss.close();
                    System.out.println("Connection closed.");
                    return;
                default:
                    out.println("Unknown command: " + clientMessage);
                    break;
            }
        }
    }
}
