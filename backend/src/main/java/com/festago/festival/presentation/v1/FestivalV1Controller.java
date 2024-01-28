package com.festago.festival.presentation.v1;

import com.festago.festival.application.FestivalV1QueryService;
import com.festago.festival.dto.FestivalV1ListRequest;
import com.festago.festival.dto.FestivalV1Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/festivals")
@RequiredArgsConstructor
public class FestivalV1Controller {

    private final FestivalV1QueryService festivalV1QueryService;

    @GetMapping
    public ResponseEntity<Slice<FestivalV1Response>> findFestivals(
        FestivalV1ListRequest festivalListRequest) {
        return ResponseEntity.ok(festivalV1QueryService.findFestivals(festivalListRequest));
    }
}
