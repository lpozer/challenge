package com.challenge.challenge.feign;

import com.challenge.challenge.model.WeatherResponse;

import java.util.Map;

public class WeatherFallback implements WeatherFeign {

    @Override
    public WeatherResponse getLocalWeather(Map<String, String> options) {
        WeatherResponse weatherResponse = new WeatherResponse();
        WeatherResponse.Main main = new WeatherResponse.Main();
        main.setTemp(20f);
        weatherResponse.setMain(main);
        return weatherResponse;
    }
}
