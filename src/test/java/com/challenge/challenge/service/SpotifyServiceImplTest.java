package com.challenge.challenge.service;

import com.challenge.challenge.feign.SpotifyFeign;
import com.challenge.challenge.model.SpotifyResponse;
import com.challenge.challenge.model.Track;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.challenge.challenge.model.MusicalGenre.ROCK;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SpotifyServiceImplTest {

    private SpotifyFeign spotifyFeign;
    private SpotifyService spotifyService;
    private HystrixFeignService hystrixFeignService;

    @Before
    public void setup() {
        hystrixFeignService = mock(HystrixFeignService.class);
        spotifyFeign = mock(SpotifyFeign.class);
        spotifyService = new SpotifyServiceImpl(hystrixFeignService);
        ReflectionTestUtils.setField(spotifyService, "recommendationUrl", "RECOMMENDATION_URL");

        when(hystrixFeignService.getApi(eq(SpotifyFeign.class), anyString(), any(SpotifyFeign.class)))
                .thenReturn(spotifyFeign);
    }

    @Test
    public void testGetTrackListByGenre() {
        SpotifyResponse spotifyResponse = mock(SpotifyResponse.class);
        ArrayList<Track> tracks = Lists.newArrayList(mock(Track.class));

        when(spotifyResponse.getTracks()).thenReturn(tracks);
        when(spotifyFeign.getRecommendation(anyMap(), anyMap())).thenReturn(spotifyResponse);

        List<Track> recommendation = spotifyService.getTrackListByGenre(ROCK, 10, null);
        assertNotNull(recommendation);
        assertEquals(tracks, recommendation);
    }

    @Test
    public void testGetTrackListFailure() throws Exception {
        when(spotifyFeign.getRecommendation(anyMap(), anyMap())).thenReturn(null);

        List<Track> recommendation = spotifyService.getTrackListByGenre(ROCK, 10, null);
        assertNull(recommendation);
    }

    @Test
    public void testGetTrackListDefaultLimit() throws Exception {
        spotifyService.getTrackListByGenre(ROCK, null, null);
        ArgumentCaptor<Map<String, String>> argumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(spotifyFeign).getRecommendation(argumentCaptor.capture(), anyMap());

        Map<String, String> query = argumentCaptor.getValue();
        assertEquals("10", query.get("limit"));
        assertEquals(ROCK.getValue(), query.get("seed_genres"));
    }

    @Test
    public void testAuthenticationHeaders() throws Exception {
        String auth = "auth";
        ArgumentCaptor<Map<String, String>> argumentCaptor = ArgumentCaptor.forClass(Map.class);

        spotifyService.getTrackListByGenre(ROCK, null, auth);
        verify(spotifyFeign).getRecommendation(anyMap(), argumentCaptor.capture());

        Map<String, String> headers = argumentCaptor.getValue();
        String expectedAuthHeader = "Bearer " + auth;
        assertEquals(expectedAuthHeader, headers.get("authorization"));
    }
}
