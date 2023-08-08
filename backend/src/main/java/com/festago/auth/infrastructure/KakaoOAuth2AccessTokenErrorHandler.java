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
            handle4xxError(statusCode, e);
            handle5xxError(statusCode);
        }
        throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private void handle4xxError(HttpStatusCode statusCode, HttpStatusCodeException e) {
        if (statusCode.is4xxClientError()) {
            KakaoOAuth2ErrorResponse errorResponse = e.getResponseBodyAs(KakaoOAuth2ErrorResponse.class);
            handleErrorCode(errorResponse);
        }
    }

    private void handleErrorCode(KakaoOAuth2ErrorResponse errorResponse) {
        handleKOE320Error(errorResponse);
        throw new InternalServerException(ErrorCode.OAUTH2_INVALID_REQUEST);
    }

    private void handleKOE320Error(KakaoOAuth2ErrorResponse errorResponse) {
        if (errorResponse != null && errorResponse.isErrorCodeKOE320()) {
            throw new BadRequestException(ErrorCode.OAUTH2_INVALID_CODE);
        }
    }

    private void handle5xxError(HttpStatusCode statusCode) {
        if (statusCode.is5xxServerError()) {
            throw new InternalServerException(ErrorCode.OAUTH2_PROVIDER_NOT_RESPONSE);
        }
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
