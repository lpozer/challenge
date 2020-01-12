package com.challenge.challenge.feign;

import com.challenge.challenge.model.SpotifyResponse;
import feign.HeaderMap;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;

@FeignClient(name = "spotify")
public interface SpotifyFeign {

    @RequestLine("GET")
    SpotifyResponse getRecommendation(@QueryMap Map<String, String> queryMap, @HeaderMap Map<String, String> headers);
}
