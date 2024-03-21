package com.festago.bookmark.presentation.v1;

import com.festago.auth.annotation.Member;
import com.festago.bookmark.application.FestivalBookmarkCommandService;
import com.festago.bookmark.application.FestivalBookmarkV1QueryService;
import com.festago.bookmark.dto.FestivalBookmarkV1Response;
import com.festago.bookmark.repository.FestivalBookmarkOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark/festivals")
@Tag(name = "축제 북마크 V1")
public class FestivalBookmarkV1Controller {

    private final FestivalBookmarkV1QueryService festivalBookmarkV1QueryService;
    private final FestivalBookmarkCommandService festivalBookmarkCommandService;

    @GetMapping("/ids")
    @Operation(description = "북마크 된 축제의 식별자 목록을 조회한다.")
    public ResponseEntity<List<Long>> findBookmarkedFestivalIds(
        @Member Long memberId
    ) {
        return ResponseEntity.ok()
            .body(festivalBookmarkV1QueryService.findBookmarkedFestivalIds(memberId));
    }

    // TODO Festival 패키지에 속해야 할 것 같은데, 북마크 최신 저장 순 정렬 조건 때문에 어쩔 수 없음...
    @GetMapping
    @Operation(description = "축제의 식별자 목록으로 북마크 된 축제의 목록을 조회한다.")
    public ResponseEntity<List<FestivalBookmarkV1Response>> findBookmarkedFestivals(
        @Member Long memberId,
        @RequestParam List<Long> festivalIds,
        @RequestParam FestivalBookmarkOrder festivalBookmarkOrder // TODO 더 좋은 이름이 없을까?
    ) {
        return ResponseEntity.ok()
            .body(festivalBookmarkV1QueryService.findBookmarkedFestivals(
                memberId,
                festivalIds,
                festivalBookmarkOrder
            ));
    }

    @PutMapping("/{festivalId}")
    @Operation(description = "축제의 식별자로 축제 북마크를 등록한다.")
    public ResponseEntity<Void> putFestivalBookmark(
        @Member Long memberId,
        @PathVariable Long festivalId
    ) {
        festivalBookmarkCommandService.putFestivalBookmark(memberId, festivalId);
        return ResponseEntity.ok() // TODO HTTP 스펙에는 PUT 응답에서 201을 보내야 하지만, 식별자로 사용할 자원이 없기에 200으로 보냄
            .build();
    }

    @DeleteMapping("/{festivalId}")
    @Operation(description = "축제의 식별자로 축제 북마크를 삭제한다.")
    public ResponseEntity<Void> deleteFestivalBookmark(
        @Member Long memberId,
        @PathVariable Long festivalId
    ) {
        festivalBookmarkCommandService.deleteFestivalBookmark(memberId, festivalId);
        return ResponseEntity.noContent()
            .build();
    }
}
