package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.dto.KakaoAccessTokenResponse;
import com.festago.auth.infrastructure.KakaoOAuth2AccessTokenErrorHandler.KakaoOAuth2ErrorResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.InternalServerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RestClientTest(KakaoOAuth2AccessTokenClient.class)
class KakaoOAuth2AccessTokenClientTest {

    private static final String ACCESS_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    @Autowired
    KakaoOAuth2AccessTokenClient kakaoOAuth2AccessTokenClient;

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 전송한_코드가_잘못됐다면_예외() throws JsonProcessingException {
        // given
        KakaoOAuth2ErrorResponse expected = new KakaoOAuth2ErrorResponse("error", "description", "KOE320");
        mockServer.expect(requestTo(ACCESS_TOKEN_URL))
            .andRespond(MockRestResponseCreators.withBadRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(expected)));

        // when & then
        assertThatThrownBy(() -> kakaoOAuth2AccessTokenClient.getAccessToken("code"))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("잘못된 인가 코드 입니다.");
    }

    @Test
    void 예상치못한_에러인_경우_서버_예외() throws JsonProcessingException {
        // given
        KakaoOAuth2ErrorResponse expected = new KakaoOAuth2ErrorResponse("error", "description", "any");
        mockServer.expect(requestTo(ACCESS_TOKEN_URL))
            .andRespond(MockRestResponseCreators.withBadRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(expected)));

        // when & then
        assertThatThrownBy(() -> kakaoOAuth2AccessTokenClient.getAccessToken("code"))
            .isInstanceOf(InternalServerException.class)
            .hasMessage("서버 내부에 문제가 발생했습니다.");
    }

    @Test
    void 성공() throws JsonProcessingException {
        // given
        KakaoAccessTokenResponse expected = new KakaoAccessTokenResponse("tokenType", "accessToken",
            100, "refreshToken", 50);
        mockServer.expect(requestTo(ACCESS_TOKEN_URL))
            .andRespond(MockRestResponseCreators.withSuccess()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(expected)));
        // when
        String actual = kakaoOAuth2AccessTokenClient.getAccessToken("code");

        // then
        assertThat(actual).isEqualTo(expected.accessToken());
    }
}
