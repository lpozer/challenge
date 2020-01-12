package com.challenge.challenge.service;

public interface HystrixFeignService {

    <T> T getApi(Class<T> apiType, String url, T fallback);
}
