package com.festago.auth.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.festago.auth.domain.OAuth2Client;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.KakaoAccessTokenResponse;
import com.festago.auth.dto.KakaoUserInfo;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2Client implements OAuth2Client {

    private static final String ACCESS_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final RestTemplate restTemplate;
    private final String grantType;
    private final String clientId;
    private final String redirectId;

    public KakaoOAuth2Client(
        @Value("${festago.oauth2.kakao.grant-type}") String grantType,
        @Value("${festago.oauth2.kakao.client-id}") String clientId,
        @Value("${festago.oauth2.kakao.redirect-uri}") String redirectUri,
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.redirectId = redirectUri;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("grant_type", grantType);
        headers.add("client_id", clientId);
        headers.add("redirect_uri", redirectId);
        headers.add("code", code);

        try {
            KakaoAccessTokenResponse response = restTemplate.postForEntity(
                ACCESS_TOKEN_URL, headers,
                KakaoAccessTokenResponse.class).getBody();
            return response.accessToken();
        } catch (HttpClientErrorException e) {
            KakaoOAuth2Error kakaoOAuth2Error = e.getResponseBodyAs(KakaoOAuth2Error.class);
            if (kakaoOAuth2Error.isErrorCodeKOE320()) {
                throw new BadRequestException(ErrorCode.OAUTH2_INVALID_CODE);
            }
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public UserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        try {
            KakaoUserInfo kakaoUserInfo = restTemplate.postForEntity(USER_INFO_URL, new HttpEntity<>(headers),
                    KakaoUserInfo.class)
                .getBody();
            return UserInfo.ofKakao(kakaoUserInfo);
        } catch (HttpClientErrorException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    public record KakaoOAuth2Error(
        String error,
        @JsonProperty("error_description") String errorDescription,
        @JsonProperty("error_code") String errorCode
    ) {

        public boolean isErrorCodeKOE320() {
            return errorCode.equals("KOE320");
        }
    }
}
