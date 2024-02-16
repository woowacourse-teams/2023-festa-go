package com.festago.artist.presentation.v1;

import com.festago.artist.application.ArtistDetailV1QueryService;
import com.festago.artist.dto.ArtistDetailV1Response;
import com.festago.artist.dto.ArtistFestivalDetailV1Response;
import com.festago.common.aop.ValidPageable;
import com.festago.common.exception.ValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
public class ArtistDetailV1Controller {

    private final ArtistDetailV1QueryService artistDetailV1QueryService;

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistDetailV1Response> getArtistInfo(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistDetailV1QueryService.findArtistDetail(artistId));
    }

    @GetMapping("/{artistId}/festivals")
    @ValidPageable(maxSize = 20)
    public ResponseEntity<Slice<ArtistFestivalDetailV1Response>> getArtistInfo(
        @PathVariable Long artistId,
        ArtistFestivalDetailV1Request request,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        validate(request);
        return ResponseEntity.ok(artistDetailV1QueryService.findArtistFestivals(artistId, request, pageable));
    }

    private void validate(ArtistFestivalDetailV1Request request) {
        validateCursor(request);
    }

    private void validateCursor(ArtistFestivalDetailV1Request request) {
        if (request.getLastStartDate() == null && request.getLastFestivalId() == null) {
            return;
        }
        if (request.getLastStartDate() != null && request.getLastFestivalId() != null) {
            return;
        }
        throw new ValidException("festivalId, lastStartDate 두 값 모두 요청하거나 요청하지 않아야합니다.");
    }
}
