package com.challenge.challenge.service;

import com.challenge.challenge.feign.SpotifyFallback;
import com.challenge.challenge.feign.SpotifyFeign;
import com.challenge.challenge.model.MusicalGenre;
import com.challenge.challenge.model.SpotifyResponse;
import com.challenge.challenge.model.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Lazy
@Service
public class SpotifyServiceImpl implements SpotifyService {

    @Value("${spotify.recommendation.url}")
    private String recommendationUrl;

    private static final String AUTHORIZATION = "authorization";
    private HystrixFeignService hystrixFeignService;

    @Autowired
    public SpotifyServiceImpl(HystrixFeignService hystrixFeignService) {
        this.hystrixFeignService = hystrixFeignService;
    }

    @Override
    public List<Track> getTrackListByGenre(MusicalGenre genre, Integer limit, String authToken) {
        SpotifyFeign spotifyFeign = hystrixFeignService.getApi(SpotifyFeign.class, recommendationUrl,
                new SpotifyFallback());
        SpotifyResponse response = spotifyFeign.getRecommendation(createQueryMap(genre.getValue(), limit),
                setAuthenticationHeader(authToken));
        return Optional.ofNullable(response).map(SpotifyResponse::getTracks).orElse(null);
    }

    private Map<String, String> setAuthenticationHeader(String token) {
        Map<String, String> header = new HashMap<>();
        header.put(AUTHORIZATION, "Bearer " + token);
        return header;
    }

    private Map<String, String> createQueryMap(String genre, Integer limit) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("limit", getRecommendationLimit(limit));
        queryMap.put("seed_genres", genre);
        return queryMap;
    }

    private String getRecommendationLimit(Integer limit) {
        return limit != null ? limit.toString() : "10";
    }
}
