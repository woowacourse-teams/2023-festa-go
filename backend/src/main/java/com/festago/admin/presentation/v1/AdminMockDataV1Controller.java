package com.festago.admin.presentation.v1;

import com.festago.mock.application.MockDataService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile({"!prod"})
@RestController
@RequestMapping("/admin/api/v1/mock-data")
@RequiredArgsConstructor
@Hidden
public class AdminMockDataV1Controller {

    private final MockDataService mockDataService;

    @PostMapping("/festivals")
    public ResponseEntity<Void> generateMockFestivals() {
        mockDataService.makeMockFestivals();
        return ResponseEntity.ok().build();
    }
}
