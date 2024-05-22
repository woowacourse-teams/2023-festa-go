package com.festago.auth.infrastructure.openid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import io.jsonwebtoken.Identifiable;
import io.jsonwebtoken.security.JwkSet;
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
@RestClientTest(AppleOpenIdJwksClient.class)
class AppleOpenIdJwksClientTest {

    private static final String URL = "https://appleid.apple.com/auth/keys";

    @Autowired
    AppleOpenIdJwksClient appleOpenIdJwksClient;

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
        assertThatThrownBy(() -> appleOpenIdJwksClient.requestGetJwks())
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
        assertThatThrownBy(() -> appleOpenIdJwksClient.requestGetJwks())
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
                   "kty": "RSA",
                   "kid": "pyaRQpAbnY",
                   "use": "sig",
                   "alg": "RS256",
                   "n": "qHiwOpizi6xHG8FIOSWH4l0P1CjLIC7aBFkhbk7BrD4s9KQAs5Sj5xAtOwlZMyP2XFcqRtZBLIMM7vw_CNERtRrhc68se5hQE_vsrHy7ugcQU6ogJS6s54zqO-zTUfaa3mABM6iR-EfgSpvz33WTQZAPtwAyxaSLknHyDzWjHEZ44WqaQBdcMAvgsWMYG5dBfnV-3Or3V2r1vdbinRE5NomE2nsKDbnJ3yo3u-x9TizKazS1JV3umt71xDqbruZLybIrimrzg_i9OSIzT2o5ZWz8zdYkKHZ4cvRPh-DDt8kV7chzR2tenPF2c5WXuK-FumOrjT7WW6uwSvhnhwNZuw",
                   "e": "AQAB"
                 },
                 {
                   "kty": "RSA",
                   "kid": "lVHdOx8ltR",
                   "use": "sig",
                   "alg": "RS256",
                   "n": "nXDu9MPf6dmVtFbDdAaal_0cO9ur2tqrrmCZaAe8TUWHU8AprhJG4DaQoCIa4UsOSCbCYOjPpPGGdE_p0XeP1ew55pBIquNhNtNNEMX0jNYAKcA9WAP1zGSkvH5m39GMFc4SsGiQ_8Szht9cayJX1SJALEgSyDOFLs-ekHnexqsr-KPtlYciwer5jaNcW3B7f9VNp1XCypQloQwSGVismPHwDJowPQ1xOWmhBLCK50NV38ZjobUDSBbCeLYecMtsdL5ZGv-iufddBh3RHszQiD2G-VXoGOs1yE33K4uAto2F2bHVcKOUy0__9qEsXZGf-B5ZOFucUkoN7T2iqu2E2Q",
                   "e": "AQAB"
                 },
                 {
                   "kty": "RSA",
                   "kid": "Bh6H7rHVmb",
                   "use": "sig",
                   "alg": "RS256",
                   "n": "2HkIZ7xKMUYH_wtt2Gwq6jXKRl-Ng5vdwd-XcWn5RIW82-uxdmGJyTo3T6MPty-xWUeW7FCs9NlM4yu02GKgwep7TKfnOovP78sd3rESbZsvuN7zD_Vk6aZP7QfqblElUtiMQxh9bu-gZUeMZfa_ndX-P5C4yKtZvXGrSPLLjyAcSmSHNLZnWbZXjeIVsgXWHMr5fwVEAkftHq_4py82xgn2XEK0FK9HmWOCZ47Wcx9fWBnqSi9JTJTUX0lh-kI5TcYfv9UKX2oe3uyOn-A460E_L_4ximlM-lgi3otw26EZfAGY9FFgSZoACjhgw_z5NRbK9dycHRpeLY9GxIyiYw",
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
        JwkSet actual = appleOpenIdJwksClient.requestGetJwks();

        // then
        assertThat(actual.getKeys())
            .map(Identifiable::getId)
            .containsExactlyInAnyOrder("pyaRQpAbnY", "lVHdOx8ltR", "Bh6H7rHVmb");
    }
}
