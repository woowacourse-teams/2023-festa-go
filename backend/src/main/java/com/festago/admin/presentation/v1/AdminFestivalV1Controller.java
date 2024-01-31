package com.festago.admin.presentation.v1;

import com.festago.admin.dto.FestivalV1CreateRequest;
import com.festago.festival.application.FestivalCommandService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/festivals")
@RequiredArgsConstructor
@Hidden
public class AdminFestivalV1Controller {

    private final FestivalCommandService festivalCommandService;

    @PostMapping
    public ResponseEntity<Void> createFestival(
        @RequestBody @Valid FestivalV1CreateRequest request
    ) {
        Long festivalId = festivalCommandService.createFestival(request.toCommand());
        return ResponseEntity.created(URI.create("/admin/api/v1/festivals/" + festivalId))
            .build();
    }
}
