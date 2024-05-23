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

@Component
@Slf4j
public class AppleOpenIdJwksClient {

    private final RestTemplate restTemplate;
    private final Parser<JwkSet> parser;

    public AppleOpenIdJwksClient(
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.restTemplate = restTemplateBuilder
            .errorHandler(new AppleOpenIdJwksErrorHandler())
            .setConnectTimeout(Duration.ofSeconds(2))
            .setReadTimeout(Duration.ofSeconds(3))
            .build();
        this.parser = Jwks.setParser()
            .build();
    }

    public JwkSet requestGetJwks() {
        try {
            String jsonKeys = restTemplate.getForObject("https://appleid.apple.com/auth/keys", String.class);
            log.info("Apple JWKS 공개키 목록을 조회했습니다.");
            return parser.parse(jsonKeys);
        } catch (ResourceAccessException e) {
            log.warn("Apple JWKS 서버가 응답하지 않습니다.");
            throw new InternalServerException(ErrorCode.OPEN_ID_PROVIDER_NOT_RESPONSE);
        }
    }
}
