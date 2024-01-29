package com.festago.festival.presentation.v1;

import com.festago.common.exception.ValidException;
import com.festago.festival.application.FestivalV1QueryService;
import com.festago.festival.dto.FestivalV1QueryRequest;
import com.festago.festival.dto.FestivalV1Response;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
        @PageableDefault(size = 10) Pageable pageable,
        FestivalV1QueryRequest request
    ) {
        validate(request.lastFestivalId(), request.lastStartDate(), pageable.getPageSize());
        var response = festivalV1QueryService.findFestivals(pageable, request);
        return ResponseEntity.ok(response);
    }

    private void validate(Long lastFestivalId, LocalDate lastStartDate, Integer limit) {
        validateCursor(lastFestivalId, lastStartDate);
        validateLimit(limit);
    }

    private void validateCursor(Long lastFestivalId, LocalDate lastStartDate) {
        if (lastFestivalId == null && lastStartDate == null) {
            return;
        }
        if (lastFestivalId != null && lastStartDate != null) {
            return;
        }
        throw new ValidException("festivalId, lastStartDate 두 값 모두 요청하거나 요청하지 않아야합니다.");
    }

    private void validateLimit(Integer limit) {
        if (limit > 20 || limit < 1) {
            throw new ValidException("페이지 갯수의 제한을 벗어납니다.");
        }
    }
}
