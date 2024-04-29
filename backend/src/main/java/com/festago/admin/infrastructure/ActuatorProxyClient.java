package com.festago.admin.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ActuatorProxyClient {

    private final RestTemplate restTemplate;
    private final int port;

    public ActuatorProxyClient(
        @Value("${management.server.port}") int port,
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.restTemplate = restTemplateBuilder
            .errorHandler(new AdminActuatorProxyErrorHandler())
            .build();
        this.port = port;
    }

    public ResponseEntity<String> request(String path) {
        return restTemplate.getForEntity("http://localhost:" + port + "/actuator/" + path, String.class);
    }
}
