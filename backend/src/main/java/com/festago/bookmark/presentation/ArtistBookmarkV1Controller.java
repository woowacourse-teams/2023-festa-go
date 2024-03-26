package com.festago.bookmark.presentation;

import com.festago.auth.annotation.Member;
import com.festago.bookmark.application.ArtistBookmarkV1QueryService;
import com.festago.bookmark.dto.v1.ArtistBookmarkV1Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "아티스트 북마크 V1")
@RequestMapping("/api/v1/bookmarks/artists")
public class ArtistBookmarkV1Controller {

    private final ArtistBookmarkV1QueryService artistBookmarkV1QueryService;

    @GetMapping
    @Operation(description = "유저의 아티스트 북마크 목록을 조회한다.", summary = "아티스트 북마크 조회")
    public ResponseEntity<List<ArtistBookmarkV1Response>> findArtistBookmarksByMemberId(@Member Long memberId) {
        return ResponseEntity.ok(artistBookmarkV1QueryService.findArtistBookmarksByMemberId(memberId));
    }
}
