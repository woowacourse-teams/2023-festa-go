package com.festago.bookmark.presentation.v1;

import com.festago.auth.annotation.Member;
import com.festago.auth.annotation.MemberAuth;
import com.festago.bookmark.application.SchoolBookmarkV1QueryService;
import com.festago.bookmark.dto.v1.SchoolBookmarkV1Response;
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
@RequestMapping("/api/v1/bookmarks/schools")
@Tag(name = "학교 북마크 요청 V1")
public class SchoolBookmarkV1Controller {

    private final SchoolBookmarkV1QueryService schoolBookmarkV1QueryService;

    @MemberAuth
    @GetMapping
    @Operation(description = "회원의 학교 북마크 목록을 조회한다", summary = "학교 북마크 조회")
    public ResponseEntity<List<SchoolBookmarkV1Response>> findAllByMemberId(@Member Long memberId) {
        return ResponseEntity.ok(schoolBookmarkV1QueryService.findAllByMemberId(memberId));
    }
}
