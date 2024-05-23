package com.festago.auth.infrastructure.oauth2;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

@Slf4j
public class KakaoOAuth2UserInfoErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        handle4xxError(statusCode);
        handle5xxError(statusCode);
        log.error("카카오 OAuth2 요청 중 알 수 없는 문제가 발생했습니다.");
        throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private void handle4xxError(HttpStatusCode statusCode) {
        if (statusCode.is4xxClientError()) {
            throw new BadRequestException(ErrorCode.OAUTH2_INVALID_TOKEN);
        }
    }

    private void handle5xxError(HttpStatusCode statusCode) {
        if (statusCode.is5xxServerError()) {
            log.warn("카카오 OAuth2 요청에 500 에러가 발생했습니다.");
            throw new InternalServerException(ErrorCode.OAUTH2_PROVIDER_NOT_RESPONSE);
        }
    }
}
