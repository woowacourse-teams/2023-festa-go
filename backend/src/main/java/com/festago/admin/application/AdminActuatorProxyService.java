package com.festago.admin.application;

import com.festago.admin.infrastructure.ActuatorProxyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminActuatorProxyService {

    private final ActuatorProxyClient actuatorProxyClient;

    public ResponseEntity<String> request(String path) {
        return actuatorProxyClient.request(path);
    }
}
