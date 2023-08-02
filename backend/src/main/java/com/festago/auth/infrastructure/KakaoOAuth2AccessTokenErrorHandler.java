package com.festago.auth.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;

public class KakaoOAuth2AccessTokenErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        try {
            super.handleError(response);
        } catch (HttpStatusCodeException e) {
            HttpStatusCode statusCode = response.getStatusCode();
            is4xxError(statusCode, e);
            is5xxError(statusCode);
        }
        throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private void is4xxError(HttpStatusCode statusCode, HttpStatusCodeException e) {
        if (statusCode.is4xxClientError()) {
            KakaoOAuth2ErrorResponse errorResponse = e.getResponseBodyAs(KakaoOAuth2ErrorResponse.class);
            isKOE320ErrorCode(errorResponse);
        }
    }

    private void is5xxError(HttpStatusCode statusCode) {
        if (statusCode.is5xxServerError()) {
            throw new InternalServerException(ErrorCode.OAUTH2_PROVIDER_NOT_RESPONSE);
        }
    }

    private void isKOE320ErrorCode(KakaoOAuth2ErrorResponse errorResponse) {
        if (errorResponse != null && errorResponse.isErrorCodeKOE320()) {
            throw new BadRequestException(ErrorCode.OAUTH2_INVALID_CODE);
        }
        throw new InternalServerException(ErrorCode.OAUTH2_INVALID_REQUEST);
    }

    public record KakaoOAuth2ErrorResponse(
        String error,
        @JsonProperty("error_description") String errorDescription,
        @JsonProperty("error_code") String errorCode
    ) {

        public boolean isErrorCodeKOE320() {
            return Objects.equals(errorCode, "KOE320");
        }
    }
}
