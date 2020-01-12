package com.challenge.challenge.service;

import com.challenge.challenge.feign.WeatherFallback;
import com.challenge.challenge.feign.WeatherFeign;
import com.challenge.challenge.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Lazy
@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${openweathermap.appid}")
    private String appId;

    @Value("${openwathermap.url}")
    private String url;

    private HystrixFeignService hystrixFeignService;

    @Autowired
    public WeatherServiceImpl(HystrixFeignService hystrixFeignService) {
        this.hystrixFeignService = hystrixFeignService;
    }

    @Override
    public float getLocalTemperature(String location) {
        WeatherFeign api = hystrixFeignService.getApi(WeatherFeign.class, url, new WeatherFallback());

        Map<String, String> map = initializeParamsMap();
        map.put("q", location);
        WeatherResponse localWeather = api.getLocalWeather(map);
        return Optional.ofNullable(localWeather).map(WeatherResponse::getMain)
                .orElseGet(WeatherResponse.Main::new).getTemp();
    }

    @Override
    public float getLocalTemperature(String lat, String lon) {
        WeatherFeign api = hystrixFeignService.getApi(WeatherFeign.class, url, new WeatherFallback());

        Map<String, String> map = initializeParamsMap();
        map.put("lat", lat);
        map.put("lon", lon);
        WeatherResponse localWeather = api.getLocalWeather(map);
        return Optional.ofNullable(localWeather).map(WeatherResponse::getMain)
                .orElseGet(WeatherResponse.Main::new).getTemp();
    }

    private Map<String, String> initializeParamsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("units", "metric");
        return map;
    }
}
