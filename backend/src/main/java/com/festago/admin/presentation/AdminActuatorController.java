package com.festago.admin.presentation;

import com.festago.admin.application.AdminActuatorProxyService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/admin/api/actuator")
@RequiredArgsConstructor
public class AdminActuatorController {

    private final AdminActuatorProxyService adminActuatorProxyService;

    @GetMapping("/{path}")
    public ResponseEntity<String> getActuator(@PathVariable String path) {
        return adminActuatorProxyService.request(path);
    }
}
