package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.dto.KakaoAccessTokenResponse;
import com.festago.auth.infrastructure.KakaoOAuth2AccessTokenErrorHandler.KakaoOAuth2ErrorResponse;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RestClientTest(KakaoOAuth2AccessTokenClient.class)
class KakaoOAuth2AccessTokenClientTest {

    private static final String URL = "https://kauth.kakao.com/oauth/token";

    @Autowired
    KakaoOAuth2AccessTokenClient kakaoOAuth2AccessTokenClient;

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 상태코드_400에서_KOE320_에러코드_이면_BadRequest_예외() throws JsonProcessingException {
        // given
        KakaoOAuth2ErrorResponse expected = new KakaoOAuth2ErrorResponse("error", "description", "KOE320");
        mockServer.expect(requestTo(URL))
            .andRespond(MockRestResponseCreators.withBadRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(expected)));

        // when & then
        assertThatThrownBy(() -> kakaoOAuth2AccessTokenClient.getAccessToken("code"))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ErrorCode.OAUTH2_INVALID_CODE.getMessage());
    }

    @Test
    void 상태코드_400에서_KOE320_에러코드가_아니면_InternalServer_예외() throws JsonProcessingException {
        // given
        KakaoOAuth2ErrorResponse expected = new KakaoOAuth2ErrorResponse("error", "description", "any");
        mockServer.expect(requestTo(URL))
            .andRespond(MockRestResponseCreators.withBadRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(expected)));

        // when & then
        assertThatThrownBy(() -> kakaoOAuth2AccessTokenClient.getAccessToken("code"))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(ErrorCode.OAUTH2_INVALID_REQUEST.getMessage());
    }

    @Test
    void 상태코드가_401이면_InternalServer_예외() {
        // given
        mockServer.expect(requestTo(URL))
            .andRespond(MockRestResponseCreators.withUnauthorizedRequest()
                .contentType(MediaType.APPLICATION_JSON));

        // when & then
        assertThatThrownBy(() -> kakaoOAuth2AccessTokenClient.getAccessToken("code"))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(ErrorCode.OAUTH2_INVALID_REQUEST.getMessage());
    }

    @Test
    void 상태코드가_500이면_InternalServer_예외() {
        // given
        mockServer.expect(requestTo(URL))
            .andRespond(MockRestResponseCreators.withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON));

        // when & then
        assertThatThrownBy(() -> kakaoOAuth2AccessTokenClient.getAccessToken("code"))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(ErrorCode.OAUTH2_PROVIDER_NOT_RESPONSE.getMessage());
    }

    @Test
    void 성공() throws JsonProcessingException {
        // given
        KakaoAccessTokenResponse expected = new KakaoAccessTokenResponse("tokenType", "accessToken",
            100, "refreshToken", 50);
        mockServer.expect(requestTo(URL))
            .andRespond(MockRestResponseCreators.withSuccess()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(expected)));
        // when
        String actual = kakaoOAuth2AccessTokenClient.getAccessToken("code");

        // then
        assertThat(actual).isEqualTo(expected.accessToken());
    }
}
