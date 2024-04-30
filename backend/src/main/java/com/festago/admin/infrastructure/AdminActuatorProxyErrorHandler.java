package com.festago.admin.infrastructure;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.common.exception.NotFoundException;
import java.io.IOException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class AdminActuatorProxyErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode.is4xxClientError()) {
            throw new NotFoundException(ErrorCode.ACTUATOR_NOT_FOUND);
        }
        throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
