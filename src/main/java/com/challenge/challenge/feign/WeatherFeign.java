package com.challenge.challenge.feign;

import com.challenge.challenge.model.WeatherResponse;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;

@FeignClient(name = "weather")
public interface WeatherFeign {

    @RequestLine("GET")
    WeatherResponse getLocalWeather(@QueryMap Map<String, String> options);

}
