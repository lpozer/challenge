package com.challenge.challenge.service;


import com.challenge.challenge.model.Track;

import java.util.List;
import java.util.Map;

public interface RecommendationService {

    List<Track> getTracksRecommendation(Map<String, String> params, Integer limit, String token);
}
