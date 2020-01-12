package com.challenge.challenge.controller;

import com.challenge.challenge.model.Track;
import com.challenge.challenge.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
public class ApiController {
    private final RecommendationService recommendationService;

    @Autowired
    public ApiController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping(path = "/recommendations")
    public List<Track> getRecommendations(@RequestParam Map<String, String> params,
                                          @RequestParam(value = "limit", required = false) Integer limit,
                                          Authentication authentication) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        return recommendationService.getTracksRecommendation(params, limit, details.getTokenValue());
    }

    @GetMapping(path = "/api/recommendations")
    public List<Track> getRecommendations(@RequestParam Map<String, String> params,
                                          @RequestParam(value = "limit", required = false) Integer limit,
                                          @RequestHeader(value="Spotify-Token") String token){
        return recommendationService.getTracksRecommendation(params, limit, token);
    }
}
