package com.challenge.challenge.controller;

import com.challenge.challenge.service.RecommendationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ApiControllerTest {

    private RecommendationService recommendationService;
    private ApiController apiController;
    private MockMvc mvc;
    public static final String TOKEN_VALUE = "token";

    @Before
    public void setup() {
        recommendationService = mock(RecommendationService.class);
        apiController = new ApiController(recommendationService);
        mvc = MockMvcBuilders.
                standaloneSetup(apiController)
                .build();
    }

    @Test
    public void testGetRecommendationsWeb() throws Exception {
        Authentication authentication = mockAuthentication();
        MultiValueMap<String, String> params = getParams();

        mvc.perform(
                get("/recommendations")
                        .params(params)
                        .principal(authentication))
                .andReturn().getResponse();
        verify(recommendationService).getTracksRecommendation(getExpectedMap(), 10, TOKEN_VALUE);
    }

    @Test
    public void testGetRecommendationsApi() throws Exception {
        MultiValueMap<String, String> params = getParams();
        Principal authentication = mockAuthentication();
        mvc.perform(
                get("/api/recommendations")
                        .params(params)
                        .principal(authentication)
                        .header("Spotify-Token", TOKEN_VALUE));
        verify(recommendationService).getTracksRecommendation(getExpectedMap(), 10, TOKEN_VALUE);
    }

    @Test
    public void testGetRecommendationsApiWithoutHeader() throws Exception {
        MultiValueMap<String, String> params = getParams();
        Principal authentication = mockAuthentication();
        MockHttpServletResponse response = mvc.perform(
                get("/api/recommendations")
                        .params(params)
                        .principal(authentication)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    private MultiValueMap<String, String> getParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("location", "campinas");
        params.add("limit", "10");
        return params;
    }

    private Authentication mockAuthentication() {
        Authentication authentication = mock(Authentication.class);
        OAuth2AuthenticationDetails details = mock(OAuth2AuthenticationDetails.class);
        when(details.getTokenValue()).thenReturn(TOKEN_VALUE);
        when(authentication.getDetails()).thenReturn(details);
        return authentication;
    }

    private Map<String, String> getExpectedMap() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("location", "campinas");
        expectedParams.put("limit", "10");
        return expectedParams;
    }
}
