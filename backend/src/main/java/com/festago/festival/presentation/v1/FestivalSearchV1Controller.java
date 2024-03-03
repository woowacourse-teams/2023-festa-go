package com.festago.festival.presentation.v1;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.application.FestivalSearchV1QueryService;
import com.festago.festival.dto.FestivalSearchV1Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
public class FestivalSearchV1Controller {

    private final FestivalSearchV1QueryService festivalSearchV1QueryService;

    @GetMapping
    @Operation(description = "아티스트를 검색한다.", summary = "아티스트 검색")
    public ResponseEntity<List<FestivalSearchV1Response>> getArtistInfo(@RequestParam String keyword) {
        validate(keyword);
        return ResponseEntity.ok(festivalSearchV1QueryService.search(keyword));
    }

    private void validate(String keyword) {
        if (keyword.isBlank()) {
            throw new BadRequestException(ErrorCode.INVALID_KEYWORD);
        }
    }
}
