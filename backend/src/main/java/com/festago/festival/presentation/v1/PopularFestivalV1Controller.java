package com.festago.festival.presentation.v1;

import com.festago.festival.application.PopularFestivalV1QueryService;
import com.festago.festival.dto.PopularFestivalsV1Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/popular/festivals")
@RequiredArgsConstructor
public class PopularFestivalV1Controller {

    private final PopularFestivalV1QueryService popularFestivalV1QueryService;

    @GetMapping
    public ResponseEntity<PopularFestivalsV1Response> popularFestivals() {
        return ResponseEntity.ok(popularFestivalV1QueryService.findPopularFestivals());
    }
}
