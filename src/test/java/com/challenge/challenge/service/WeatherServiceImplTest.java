package com.challenge.challenge.service;

import com.challenge.challenge.feign.WeatherFeign;
import com.challenge.challenge.model.WeatherResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherServiceImplTest {

    private WeatherFeign weatherFeign;

    private WeatherServiceImpl weatherService;

    private HystrixFeignService hystrixFeignService;

    @Before
    public void setup() {
        weatherFeign = mock(WeatherFeign.class);
        hystrixFeignService = mock(HystrixFeignService.class);
        weatherService = new WeatherServiceImpl(hystrixFeignService);
        ReflectionTestUtils.setField(weatherService, "url", "URL");
        when(hystrixFeignService.getApi(eq(WeatherFeign.class), anyString(), any(WeatherFeign.class)))
                .thenReturn(weatherFeign);
    }

    @Test
    public void testGetLocationTemperature() {
        WeatherResponse weatherResponse = mock(WeatherResponse.class);
        WeatherResponse.Main main = mock(WeatherResponse.Main.class);
        float temp = 22f;
        when(main.getTemp()).thenReturn(temp);
        when(weatherResponse.getMain()).thenReturn(main);
        when(weatherFeign.getLocalWeather(anyMap())).thenReturn(weatherResponse);

        float temperature = weatherService.getLocalTemperature("campinas");
        assertEquals(temp, temperature, 0);
    }

    @Test
    public void testGetLocationTemperatureNull() {
        float temp = 0f;
        when(weatherFeign.getLocalWeather(anyMap())).thenReturn(null);

        float temperature = weatherService.getLocalTemperature("campinas");
        assertEquals(temp, temperature, 0);
    }

    @Test
    public void testGetCoordinatesTemperature() {
        WeatherResponse weatherResponse = mock(WeatherResponse.class);
        WeatherResponse.Main main = mock(WeatherResponse.Main.class);
        float temp = 22f;
        when(main.getTemp()).thenReturn(temp);
        when(weatherResponse.getMain()).thenReturn(main);
        when(weatherFeign.getLocalWeather(anyMap())).thenReturn(weatherResponse);

        float temperature = weatherService.getLocalTemperature("lat", "lon");
        assertEquals(temp, temperature, 0);
    }

    @Test
    public void testGetCoordinatesTemperatureNull() {
        float temp = 0f;
        when(weatherFeign.getLocalWeather(anyMap())).thenReturn(null);

        float temperature = weatherService.getLocalTemperature("lat", "lon");
        assertEquals(temp, temperature, 0);
    }
}
