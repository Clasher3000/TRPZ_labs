package org.example.server.command;

import org.example.server.MusicPlayer;

import java.io.PrintWriter;
import java.util.Random;

public class RadioCommand implements Command {

    private MusicPlayer musicPlayer;
    private String name;
    private PrintWriter out;

    private String[] stations = {
            "KEXP",
            "HitFM",
            "Rocks",
            "ClassicFM",
            "Paradise"
    };

    private String[] stationUrls = {
            "http://live-aacplus-64.kexp.org/kexp64.aac",
            "http://online.hitfm.ua/HitFM",
            "http://online.radioroks.ua/RadioROKS",
            "http://media-ice.musicradio.com/ClassicFMMP3",
            "http://stream.radioparadise.com/mp3-192"
    };

    public RadioCommand(MusicPlayer musicPlayer, String name, PrintWriter out) {
        this.musicPlayer = musicPlayer;
        this.name = name;
        this.out = out;
    }

    @Override
    public void execute() {
        musicPlayer.clearPlaylist();

        if (name != null && !name.isEmpty()) {
            switch (name) {
                case "KEXP":
                    musicPlayer.playRadio("http://live-aacplus-64.kexp.org/kexp64.aac");
                    out.println("Playing radio: " + name);
                    break;
                case "HitFM":
                    musicPlayer.playRadio("http://online.hitfm.ua/HitFM");
                    out.println("Playing radio: " + name);
                    break;
                case "Rocks":
                    musicPlayer.playRadio("http://online.radioroks.ua/RadioROKS");
                    out.println("Playing radio: " + name);
                    break;
                case "ClassicFM":
                    musicPlayer.playRadio("http://media-ice.musicradio.com/ClassicFMMP3");
                    out.println("Playing radio: " + name);
                    break;
                case "Paradise":
                    musicPlayer.playRadio("http://stream.radioparadise.com/mp3-192");
                    out.println("Playing radio: " + name);
                    break;
                default:
                    out.println("Please provide a correct radio name. Options: KEXP, HitFM, Rocks, ClassicFM, Paradise.");
            }
        } else {
            Random random = new Random();
            int randomIndex = random.nextInt(stations.length);
            String randomStation = stations[randomIndex];
            String randomStationUrl = stationUrls[randomIndex];

            out.println("No radio name provided. Playing radio: " + randomStation);
            musicPlayer.playRadio(randomStationUrl);
        }
    }
}
