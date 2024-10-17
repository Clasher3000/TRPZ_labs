package com.example.MP3player.repository;

import com.example.MP3player.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Integer> {

}
