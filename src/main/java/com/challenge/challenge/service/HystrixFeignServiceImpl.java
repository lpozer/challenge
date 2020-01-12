package com.challenge.challenge.service;

import feign.form.FormEncoder;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class HystrixFeignServiceImpl implements HystrixFeignService {

    @Override
    public <T> T getApi(Class<T> apiType, String url, T fallback) {
        return HystrixFeign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new FormEncoder())
                .target(apiType, url, fallback);
    }
}
