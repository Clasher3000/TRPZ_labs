package com.example.MP3player.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @ManyToOne
    private Playlist playlist;
    private String title;
    private String path;
    private int position;
}
