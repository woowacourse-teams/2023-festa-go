package com.festago.auth.infrastructure.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.auth.dto.KakaoOAuth2TokenResponse;
import com.festago.auth.infrastructure.oauth2.KakaoOAuth2AccessTokenErrorHandler.KakaoOAuth2ErrorResponse;
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
@RestClientTest(KakaoOAuth2TokenClient.class)
class KakaoOAuth2TokenClientTest {

    private static final String URL = "https://kauth.kakao.com/oauth/token";

    @Autowired
    KakaoOAuth2TokenClient kakaoOAuth2TokenClient;

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
        assertThatThrownBy(() -> kakaoOAuth2TokenClient.getIdToken("code"))
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
        assertThatThrownBy(() -> kakaoOAuth2TokenClient.getIdToken("code"))
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
        assertThatThrownBy(() -> kakaoOAuth2TokenClient.getIdToken("code"))
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
        assertThatThrownBy(() -> kakaoOAuth2TokenClient.getIdToken("code"))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(ErrorCode.OAUTH2_PROVIDER_NOT_RESPONSE.getMessage());
    }

    @Test
    void 성공() throws JsonProcessingException {
        // given
        KakaoOAuth2TokenResponse expected = new KakaoOAuth2TokenResponse("accessToken", "idToken");
        mockServer.expect(requestTo(URL))
            .andRespond(MockRestResponseCreators.withSuccess()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(expected)));
        // when
        String actual = kakaoOAuth2TokenClient.getIdToken("code");

        // then
        assertThat(actual).isEqualTo(expected.idToken());
    }
}
