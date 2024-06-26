package com.festago.artist.presentation.v1;

import com.festago.artist.application.ArtistDetailV1QueryService;
import com.festago.artist.dto.ArtistDetailV1Response;
import com.festago.artist.dto.ArtistFestivalV1Response;
import com.festago.common.aop.ValidPageable;
import com.festago.common.dto.SliceResponse;
import com.festago.common.exception.ValidException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/artists")
@Tag(name = "아티스트 정보 요청 V1")
@RequiredArgsConstructor
public class ArtistV1Controller {

    private final ArtistDetailV1QueryService artistDetailV1QueryService;

    @GetMapping("/{artistId}")
    @Operation(description = "아티스트의 정보를 조회한다.", summary = "아티스트 정보 조회")
    public ResponseEntity<ArtistDetailV1Response> findArtistDetail(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistDetailV1QueryService.findArtistDetail(artistId));
    }

    @GetMapping("/{artistId}/festivals")
    @Operation(description = "아티스트가 참여한 축제 목록을 조회한다. isPast 값으로 종료 축제와 진행, 예정 축제를 구분 가능하다.", summary = "아티스트 참여 축제 목록 조회")
    @ValidPageable(maxSize = 20)
    public ResponseEntity<SliceResponse<ArtistFestivalV1Response>> findArtistFestivals(
        @PathVariable Long artistId,
        @RequestParam(required = false) Long lastFestivalId,
        @RequestParam(required = false) LocalDate lastStartDate,
        @RequestParam(required = false, defaultValue = "false") boolean isPast,
        @Parameter(description = "0 < size <= 20") @RequestParam(defaultValue = "10") int size
    ) {
        validate(lastFestivalId, lastStartDate);
        Slice<ArtistFestivalV1Response> response = artistDetailV1QueryService.findArtistFestivals(artistId,
            lastFestivalId, lastStartDate, isPast, PageRequest.ofSize(size));
        return ResponseEntity.ok(SliceResponse.from(response));
    }

    private void validate(Long lastFestivalId, LocalDate lastStartDate) {
        validateCursor(lastFestivalId, lastStartDate);
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
}
