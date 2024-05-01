package com.festago.auth.infrastructure;

import com.festago.auth.dto.KakaoAccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2AccessTokenClient {

    private static final String URL = "https://kauth.kakao.com/oauth/token";

    private final RestTemplate restTemplate;
    private final String grantType;
    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;

    public KakaoOAuth2AccessTokenClient(
        @Value("${festago.oauth2.kakao.grant-type}") String grantType,
        @Value("${festago.oauth2.kakao.rest-api-key}") String clientId,
        @Value("${festago.oauth2.kakao.redirect-uri}") String redirectUri,
        @Value("${festago.oauth2.kakao.client-secret}") String clientSecret,
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.restTemplate = restTemplateBuilder
            .errorHandler(new KakaoOAuth2AccessTokenErrorHandler())
            .build();
    }

    public String getAccessToken(String code) {
        HttpHeaders headers = getAccessTokenHeaders(code);
        return requestAccessToken(headers);
    }

    private HttpHeaders getAccessTokenHeaders(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("grant_type", grantType);
        headers.set("client_id", clientId);
        headers.set("redirect_uri", redirectUri);
        headers.set("client_secret", clientSecret);
        headers.set("code", code);
        return headers;
    }

    private String requestAccessToken(HttpHeaders headers) {
        KakaoAccessTokenResponse response = restTemplate.postForEntity(URL, headers,
            KakaoAccessTokenResponse.class).getBody();
        return response.accessToken();
    }
}
