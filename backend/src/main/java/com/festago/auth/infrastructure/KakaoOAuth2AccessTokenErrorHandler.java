package com.festago.auth.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;

public class KakaoOAuth2AccessTokenErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        try {
            super.handleError(response);
        } catch (HttpClientErrorException e) {
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                KakaoOAuth2ErrorResponse errorResponse = e.getResponseBodyAs(KakaoOAuth2ErrorResponse.class);
                if (errorResponse.isErrorCodeKOE320()) {
                    throw new BadRequestException(ErrorCode.OAUTH2_INVALID_CODE);
                }
            }
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR, e);
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
