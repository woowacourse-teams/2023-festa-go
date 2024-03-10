package com.festago.artist.presentation.v1;


import com.festago.artist.application.ArtistV1SearchQueryService;
import com.festago.artist.dto.ArtistSearchTotalResponse;
import com.festago.common.util.Validator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search/artists")
@Tag(name = "아티스트 검색 V1")
@RequiredArgsConstructor
public class ArtistSearchV1Controller {

    private final ArtistV1SearchQueryService artistV1SearchQueryService;

    @GetMapping
    @Operation(description = "키워드로 아티스트 목록을 검색한다", summary = "아티스트 목록 검색 조회")
    public ResponseEntity<List<ArtistSearchTotalResponse>> searchByKeyword(@RequestParam String keyword) {
        Validator.notBlank(keyword, "keyword");
        LocalDate today = LocalDate.now();
        List<ArtistSearchTotalResponse> response = artistV1SearchQueryService.findAllByKeyword(keyword, today);
        return ResponseEntity.ok(response);
    }
}
