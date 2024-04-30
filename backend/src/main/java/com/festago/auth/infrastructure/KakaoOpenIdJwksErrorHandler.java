package com.festago.auth.infrastructure;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

@Slf4j
public class KakaoOpenIdJwksErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode.isError()) {
            log.warn("카카오 JWKS 서버에서 {} 상태코드가 반환되었습니다.", statusCode.value());
            throw new InternalServerException(ErrorCode.OPEN_ID_PROVIDER_NOT_RESPONSE);
        }
        log.error("카카오 JWKS 서버에서 알 수 없는 에러가 발생했습니다.");
        throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
