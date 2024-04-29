package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import io.jsonwebtoken.Identifiable;
import io.jsonwebtoken.security.JwkSet;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RestClientTest(KakaoOpenIdJwksClient.class)
class KakaoOpenIdJwksClientTest {

    private static final String URL = "https://kauth.kakao.com/.well-known/jwks.json";

    @Autowired
    KakaoOpenIdJwksClient kakaoOpenIdJwksClient;

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 상태코드가_4xx이면_InternalServer_예외() {
        // given
        mockServer.expect(requestTo(URL))
            .andRespond(MockRestResponseCreators.withBadRequest()
                .contentType(MediaType.APPLICATION_JSON));

        // when & then
        assertThatThrownBy(() -> kakaoOpenIdJwksClient.requestGetJwks())
            .isInstanceOf(InternalServerException.class)
            .hasMessage(ErrorCode.OPEN_ID_PROVIDER_NOT_RESPONSE.getMessage());
    }

    @Test
    void 상태코드가_5xx이면_InternalServer_예외() {
        // given
        mockServer.expect(requestTo(URL))
            .andRespond(MockRestResponseCreators.withServerError()
                .contentType(MediaType.APPLICATION_JSON));

        // when & then
        assertThatThrownBy(() -> kakaoOpenIdJwksClient.requestGetJwks())
            .isInstanceOf(InternalServerException.class)
            .hasMessage(ErrorCode.OPEN_ID_PROVIDER_NOT_RESPONSE.getMessage());
    }

    @Test
    void 성공() {
        // given
        String jwksJson = """
            {
                "keys": [
                    {
                        "kid": "3f96980381e451efad0d2ddd30e3d3",
                        "kty": "RSA",
                        "alg": "RS256",
                        "use": "sig",
                        "n": "q8zZ0b_MNaLd6Ny8wd4cjFomilLfFIZcmhNSc1ttx_oQdJJZt5CDHB8WWwPGBUDUyY8AmfglS9Y1qA0_fxxs-ZUWdt45jSbUxghKNYgEwSutfM5sROh3srm5TiLW4YfOvKytGW1r9TQEdLe98ork8-rNRYPybRI3SKoqpci1m1QOcvUg4xEYRvbZIWku24DNMSeheytKUz6Ni4kKOVkzfGN11rUj1IrlRR-LNA9V9ZYmeoywy3k066rD5TaZHor5bM5gIzt1B4FmUuFITpXKGQZS5Hn_Ck8Bgc8kLWGAU8TzmOzLeROosqKE0eZJ4ESLMImTb2XSEZuN1wFyL0VtJw",
                        "e": "AQAB"
                    },
                    {
                        "kid": "9f252dadd5f233f93d2fa528d12fea",
                        "kty": "RSA",
                        "alg": "RS256",
                        "use": "sig",
                        "n": "qGWf6RVzV2pM8YqJ6by5exoixIlTvdXDfYj2v7E6xkoYmesAjp_1IYL7rzhpUYqIkWX0P4wOwAsg-Ud8PcMHggfwUNPOcqgSk1hAIHr63zSlG8xatQb17q9LrWny2HWkUVEU30PxxHsLcuzmfhbRx8kOrNfJEirIuqSyWF_OBHeEgBgYjydd_c8vPo7IiH-pijZn4ZouPsEg7wtdIX3-0ZcXXDbFkaDaqClfqmVCLNBhg3DKYDQOoyWXrpFKUXUFuk2FTCqWaQJ0GniO4p_ppkYIf4zhlwUYfXZEhm8cBo6H2EgukntDbTgnoha8kNunTPekxWTDhE5wGAt6YpT4Yw",
                        "e": "AQAB"
                    }
                ]
            }
            """;
        mockServer.expect(requestTo(URL))
            .andRespond(MockRestResponseCreators.withSuccess()
                .body(jwksJson)
                .contentType(MediaType.APPLICATION_JSON));

        // when
        JwkSet actual = kakaoOpenIdJwksClient.requestGetJwks();

        // then
        assertThat(actual.getKeys())
            .map(Identifiable::getId)
            .containsExactlyInAnyOrder("3f96980381e451efad0d2ddd30e3d3", "9f252dadd5f233f93d2fa528d12fea");
    }
}
