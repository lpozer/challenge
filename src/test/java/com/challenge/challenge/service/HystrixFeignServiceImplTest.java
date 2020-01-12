package com.challenge.challenge.service;

import com.challenge.challenge.feign.SpotifyFallback;
import com.challenge.challenge.feign.SpotifyFeign;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class HystrixFeignServiceImplTest {

    private HystrixFeignService hystrixFeignService = new HystrixFeignServiceImpl();

    @Test
    public void testGetApi() {
        SpotifyFeign api = hystrixFeignService.getApi(SpotifyFeign.class, "test", new SpotifyFallback());
        assertNotNull(api);
    }
}
