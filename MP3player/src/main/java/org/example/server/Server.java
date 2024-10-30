package org.example.server;

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

        musicPlayer = new MusicPlayer();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        String clientMessage;
        while ((clientMessage = in.readLine()) != null) {
            System.out.println("Received from client: " + clientMessage);

            String[] parts = clientMessage.split(" ", 2); // Розділити на команду і аргумент

            String command = parts[0].toLowerCase(); // Перша частина - команда
            String argument = parts.length > 1 ? parts[1] : ""; // Друга частина - аргумент (назва пісні)

            switch (command) {
                case "help":
                    out.println("play <playlist_name> to play playlist");
                    out.println("stop to stop music");
                    break;
                case "time":
                    out.println("Server time: " + java.time.LocalTime.now());
                    break;
                case "date":
                    out.println("Server date: " + java.time.LocalDate.now());
                    break;
                case "play": // Команда для відтворення пісні
                    if (!argument.isEmpty()) {
                        out.println("Playing song: " + argument);
                        musicPlayer.playSong(argument); // Передати назву пісні
                    } else {
                        out.println("No song specified. Please provide a song name.");
                    }
                    break;
                case "stop": // Команда для зупинки пісні
                    musicPlayer.stopSong(); // Додайте метод stopSong() у MusicPlayer
                    out.println("Song stopped.");
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
