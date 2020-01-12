package com.challenge.challenge.service;

import com.challenge.challenge.model.MusicalGenre;
import com.challenge.challenge.model.Track;

import java.util.List;

public interface SpotifyService {

    List<Track> getTrackListByGenre(MusicalGenre genre, Integer limit, String authToken);

}
