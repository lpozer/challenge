package com.challenge.challenge.service;

public interface WeatherService {

    float getLocalTemperature(String location);

    float getLocalTemperature(String lat, String lon);
}
