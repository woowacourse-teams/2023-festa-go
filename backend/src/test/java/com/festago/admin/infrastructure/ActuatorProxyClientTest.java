package com.festago.admin.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.common.exception.NotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RestClientTest(ActuatorProxyClient.class)
class ActuatorProxyClientTest {

    private static final String URI = "http://localhost:8090/actuator/health";

    @Autowired
    ActuatorProxyClient actuatorProxyClient;

    @Autowired
    MockRestServiceServer mockServer;

    @Test
    void 상태코드가_4xx이면_NotFound_예외() {
        // given
        mockServer.expect(requestTo(URI))
            .andRespond(MockRestResponseCreators.withBadRequest()
                .contentType(MediaType.APPLICATION_JSON));

        // when & then
        assertThatThrownBy(() -> actuatorProxyClient.request("health"))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorCode.ACTUATOR_NOT_FOUND.getMessage());
    }

    @Test
    void 상태코드가_5xx이면_InternalServer_예외() {
        // given
        mockServer.expect(requestTo(URI))
            .andRespond(MockRestResponseCreators.withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON));

        // when & then
        assertThatThrownBy(() -> actuatorProxyClient.request("health"))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void 성공() throws JsonProcessingException {
        // given
        mockServer.expect(requestTo(URI))
            .andRespond(MockRestResponseCreators.withSuccess()
                .body("data")
                .contentType(MediaType.APPLICATION_JSON));

        // when
        var response = actuatorProxyClient.request("health");

        // then
        assertThat(response.getBody()).isEqualTo("data");
    }
}
