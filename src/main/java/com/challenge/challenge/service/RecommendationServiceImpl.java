package com.challenge.challenge.service;

import com.challenge.challenge.model.MusicalGenre;
import com.challenge.challenge.model.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.challenge.challenge.model.MusicalGenre.*;


@Service
@Lazy
public class RecommendationServiceImpl implements RecommendationService {
    private static final String LOCATION = "location";
    private static final String LAT = "lat";
    private static final String LON = "lon";

    private SpotifyService spotifyService;
    private WeatherService weatherService;

    @Autowired
    public RecommendationServiceImpl(SpotifyService spotifyService, WeatherService weatherService) {
        this.spotifyService = spotifyService;
        this.weatherService = weatherService;
    }

    @Override
    public List<Track> getTracksRecommendation(Map<String, String> params, Integer limit, String token) {
        float temperature = getTemperature(params);
        MusicalGenre genre = getGenre(temperature);
        return spotifyService.getTrackListByGenre(genre, limit, token);
    }

    private MusicalGenre getGenre(float temperature) {
        if (temperature > 30) {
            return PARTY;
        }
        if (temperature >= 15 && temperature <= 30) {
            return POP;
        }
        if (temperature >= 10 && temperature < 15) {
            return ROCK;
        }
        return CLASSICAL;
    }

    private void validateParameters(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            throw new IllegalArgumentException("You must pass city name or coordinates");
        }
        if (params.containsKey(LOCATION)) {
            return;
        }
        if (params.containsKey(LAT) && !params.containsKey(LON)) {
            throw new IllegalArgumentException("You passed only lat coordinate, it's necessary lon coordinate as well");
        }
        if (params.containsKey(LON) && !params.containsKey(LAT)) {
            throw new IllegalArgumentException("You passed only lon coordinate, it's necessary lat coordinate as well");
        }
    }

    private float getTemperature(Map<String, String> params) {
        validateParameters(params);
        if (params.containsKey(LOCATION)) {
            return weatherService.getLocalTemperature(params.get(LOCATION));
        }
        return weatherService.getLocalTemperature(params.get(LAT), params.get(LON));
    }
}
