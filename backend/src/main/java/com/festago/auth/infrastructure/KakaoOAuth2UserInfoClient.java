package com.festago.auth.infrastructure;

import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.KakaoUserInfo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2UserInfoClient {

    private static final String URL = "https://kapi.kakao.com/v2/user/me";

    private final RestTemplate restTemplate;

    public KakaoOAuth2UserInfoClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
            .errorHandler(new KakaoOAuth2UserInfoErrorHandler())
            .build();
    }

    public UserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = getUserInfoHeaders(accessToken);
        return requestUserInfo(headers);
    }

    private HttpHeaders getUserInfoHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);
        return headers;
    }

    private UserInfo requestUserInfo(HttpHeaders headers) {
        KakaoUserInfo kakaoUserInfo = restTemplate.postForEntity(URL, new HttpEntity<>(headers),
                KakaoUserInfo.class)
            .getBody();
        return kakaoUserInfo.toUserInfo();
    }
}
