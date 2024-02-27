package com.festago.artist.presentation.v1;

import com.festago.artist.application.ArtistSearchV1QueryService;
import com.festago.artist.dto.ArtistsSearchV1Response;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search/artists")
@Tag(name = "아티스트 검색 요청 V1")
@RequiredArgsConstructor
public class ArtistSearchV1Controller {

    private final ArtistSearchV1QueryService artistSearchV1QueryService;

    @GetMapping
    @Operation(description = "아티스트를 검색한다.", summary = "아티스트 검색")
    public ResponseEntity<ArtistsSearchV1Response> getArtistInfo(@RequestParam String keyword) {
        validate(keyword);
        return makeResponse(artistSearchV1QueryService.search(keyword));
    }

    private void validate(String keyword) {
        if (keyword.isBlank()) {
            throw new BadRequestException(ErrorCode.INVALID_KEYWORD);
        }
    }

    private ResponseEntity<ArtistsSearchV1Response> makeResponse(Optional<ArtistsSearchV1Response> result) {
        return result.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
