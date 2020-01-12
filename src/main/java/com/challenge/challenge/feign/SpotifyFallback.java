package com.challenge.challenge.feign;


import com.challenge.challenge.model.SpotifyResponse;

import java.util.Map;

import static java.util.Collections.EMPTY_LIST;

public class SpotifyFallback implements SpotifyFeign {

    @Override
    public SpotifyResponse getRecommendation(Map<String, String> queryMap, Map<String, String> headers) {
        SpotifyResponse spotifyResponse = new SpotifyResponse();
        spotifyResponse.setTracks(EMPTY_LIST);
        return spotifyResponse;
    }
}
