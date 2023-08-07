package com.festago.auth.infrastructure;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import java.io.IOException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class KakaoOAuth2UserInfoErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        handle4xxError(statusCode);
        handle5xxError(statusCode);
        throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private void handle4xxError(HttpStatusCode statusCode) {
        if (statusCode.is4xxClientError()) {
            throw new InternalServerException(ErrorCode.OAUTH2_INVALID_REQUEST);
        }
    }

    private void handle5xxError(HttpStatusCode statusCode) {
        if (statusCode.is5xxServerError()) {
            throw new InternalServerException(ErrorCode.OAUTH2_PROVIDER_NOT_RESPONSE);
        }
    }
}
