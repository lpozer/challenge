package com.challenge.challenge.service;

import com.challenge.challenge.model.MusicalGenre;
import com.challenge.challenge.model.Track;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.challenge.challenge.model.MusicalGenre.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RecommendationServiceImplTest {

    private static final String LOCATION = "campinas";
    private static final Integer LIMIT = 10;
    public static final String TOKEN_AUTH = "tokenAuth";

    private SpotifyService spotifyService;

    private WeatherService weatherService;

    private RecommendationServiceImpl recommendationService;

    @Before
    public void setup() {
        spotifyService = mock(SpotifyService.class);
        weatherService = mock(WeatherService.class);
        recommendationService = new RecommendationServiceImpl(spotifyService, weatherService);
    }

    @Test
    public void testGetTrackRecommendationsByLocation() {
        List<Track> trackList = Lists.list(mock(Track.class));
        Map<String, String> params = new HashMap<>();
        params.put("location", LOCATION);
        when(weatherService.getLocalTemperature(LOCATION)).thenReturn(22f);
        when(spotifyService.getTrackListByGenre(any(MusicalGenre.class), eq(LIMIT), eq(TOKEN_AUTH))).thenReturn(trackList);

        List<Track> recommendation = recommendationService.getTracksRecommendation(params, LIMIT, TOKEN_AUTH);
        Assert.assertEquals(trackList, recommendation);
    }

    @Test
    public void testGetTrackRecommendationsByLatAndLon()  {
        List<Track> trackList = Lists.list(mock(Track.class));
        Map<String, String> params = new HashMap<>();
        params.put("lat", "10");
        params.put("lon", "20");
        when(weatherService.getLocalTemperature("10", "20")).thenReturn(22f);
        when(spotifyService.getTrackListByGenre(any(MusicalGenre.class), eq(LIMIT), eq(TOKEN_AUTH))).thenReturn(trackList);

        List<Track> recommendation = recommendationService.getTracksRecommendation(params, LIMIT, TOKEN_AUTH);
        Assert.assertEquals(trackList, recommendation);
    }

    @Test
    public void testGetPartyTracks() {
        Map<String, String> params = new HashMap<>();
        params.put("location", LOCATION);
        when(weatherService.getLocalTemperature(LOCATION)).thenReturn(31f);

        recommendationService.getTracksRecommendation(params, LIMIT, TOKEN_AUTH);
        verify(spotifyService).getTrackListByGenre(eq(PARTY), eq(LIMIT), eq(TOKEN_AUTH));
    }

    @Test
    public void testGetPopTracks() {
        Map<String, String> params = new HashMap<>();
        params.put("location", LOCATION);
        when(weatherService.getLocalTemperature(LOCATION)).thenReturn(30f);

        recommendationService.getTracksRecommendation(params, LIMIT, TOKEN_AUTH);
        verify(spotifyService).getTrackListByGenre(eq(POP), eq(LIMIT), eq(TOKEN_AUTH));

        when(weatherService.getLocalTemperature(LOCATION)).thenReturn(15f);

        recommendationService.getTracksRecommendation(params, LIMIT, TOKEN_AUTH);
        verify(spotifyService, times(2)).getTrackListByGenre(eq(POP), eq(LIMIT), eq(TOKEN_AUTH));
    }

    @Test
    public void testGetRockTracks() {
        Map<String, String> params = new HashMap<>();
        params.put("location", LOCATION);
        when(weatherService.getLocalTemperature(LOCATION)).thenReturn(14f);

        recommendationService.getTracksRecommendation(params, LIMIT, TOKEN_AUTH);
        verify(spotifyService).getTrackListByGenre(eq(ROCK), eq(LIMIT), eq(TOKEN_AUTH));

        when(weatherService.getLocalTemperature(LOCATION)).thenReturn(10f);

        recommendationService.getTracksRecommendation(params, LIMIT, TOKEN_AUTH);
        verify(spotifyService, times(2)).getTrackListByGenre(eq(ROCK), eq(LIMIT), eq(TOKEN_AUTH));
    }

    @Test
    public void testGetClassicalTracks() {
        Map<String, String> params = new HashMap<>();
        params.put("location", LOCATION);
        when(weatherService.getLocalTemperature(LOCATION)).thenReturn(9f);

        recommendationService.getTracksRecommendation(params, LIMIT, TOKEN_AUTH);
        verify(spotifyService).getTrackListByGenre(eq(CLASSICAL), eq(LIMIT), eq(TOKEN_AUTH));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetTracksWithoutParameter() {
        Map<String, String> params = new HashMap<>();
        recommendationService.getTracksRecommendation(params, LIMIT, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetTracksWithOnlyLatParameter() {
        Map<String, String> params = new HashMap<>();
        params.put("lat", "10");
        recommendationService.getTracksRecommendation(params, LIMIT, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetTracksWithOnlyLonParameter() {
        Map<String, String> params = new HashMap<>();
        params.put("lon", "10");
        recommendationService.getTracksRecommendation(params, LIMIT, null);
    }
}
