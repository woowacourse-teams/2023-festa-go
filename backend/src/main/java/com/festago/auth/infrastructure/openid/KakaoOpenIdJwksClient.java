package com.festago.auth.infrastructure.openid;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import io.jsonwebtoken.io.Parser;
import io.jsonwebtoken.security.JwkSet;
import io.jsonwebtoken.security.Jwks;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoOpenIdJwksClient {

    private final RestTemplate restTemplate;
    private final Parser<JwkSet> parser;

    public KakaoOpenIdJwksClient(
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.restTemplate = restTemplateBuilder
            .errorHandler(new KakaoOpenIdJwksErrorHandler())
            .setConnectTimeout(Duration.ofSeconds(2))
            .setReadTimeout(Duration.ofSeconds(3))
            .build();
        this.parser = Jwks.setParser()
            .build();
    }

    // 너무 많은 요청이 오면 차단될 수 있음
    public JwkSet requestGetJwks() {
        try {
            String jsonKeys = restTemplate.getForObject("https://kauth.kakao.com/.well-known/jwks.json", String.class);
            log.info("카카오 JWKS 공개키 목록을 조회했습니다.");
            return parser.parse(jsonKeys);
        } catch (ResourceAccessException e) {
            log.warn("카카오 JWKS 서버가 응답하지 않습니다.");
            throw new InternalServerException(ErrorCode.OPEN_ID_PROVIDER_NOT_RESPONSE);
        }
    }
}
