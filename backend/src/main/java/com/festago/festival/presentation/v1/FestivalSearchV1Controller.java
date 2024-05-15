package com.festago.festival.presentation.v1;

import com.festago.common.util.Validator;
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
@RequestMapping("/api/v1/search/festivals")
@Tag(name = "축제 검색 요청 V1")
@RequiredArgsConstructor
public class FestivalSearchV1Controller {

    private final FestivalSearchV1QueryService festivalSearchV1QueryService;

    @GetMapping
    @Operation(description = "키워드로 축제를 검색한다. ~대, ~대학교로 끝날 시 대학교 축제 검색, 그 외의 경우 아티스트가 참여한 축제 검색.", summary = "축제 검색")
    public ResponseEntity<List<FestivalSearchV1Response>> searchFestivals(@RequestParam String keyword) {
        validate(keyword);
        return ResponseEntity.ok(festivalSearchV1QueryService.search(keyword));
    }

    private void validate(String keyword) {
        Validator.notBlank(keyword, "keyword");
    }
}
