package org.example.visitor;

import org.example.entity.Playlist;
import org.example.entity.Track;
import org.example.service.PlaylistService;
import org.example.service.TrackService;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FindVisitor implements Visitor{

    private PrintWriter out;

    public FindVisitor(PrintWriter out) {
        this.out = out;
    }

    public void findAll(Element... args) {
        for (Element element : args) {
            List<String> result = element.accept(this);
            for (String item : result) {
                out.println(item);
            }
        }
    }

    @Override
    public List<String> visitTrack(TrackService trackService) {
        List<Track> tracks = trackService.findAll();
        List<String> trackTitles = new ArrayList<>();
        for (Track track : tracks
             ) {
            trackTitles.add(track.getTitle() + " - Track");
        }
        return trackTitles;
    }

    @Override
    public List<String> visitPlaylist(PlaylistService playlistService) {
        List<Playlist> playlists = playlistService.findAll();
        List<String> playlistsNames = new ArrayList<>();

        for (Playlist playlist : playlists) {
            // Створюємо список імен треків за допомогою стрімів
            String playlistTracks = playlist.getTracks().stream()
                    .map(Track::getTitle)
                    .collect(Collectors.joining(", ", " [", "]"));

            // Додаємо назву плейлиста з треками до списку
            playlistsNames.add(playlist.getName() + playlistTracks);
        }
        return playlistsNames;
    }
}
