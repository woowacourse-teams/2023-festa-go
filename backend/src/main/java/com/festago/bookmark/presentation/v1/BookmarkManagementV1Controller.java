package com.festago.bookmark.presentation.v1;

import com.festago.auth.annotation.Member;
import com.festago.auth.annotation.MemberAuth;
import com.festago.bookmark.application.command.BookmarkFacadeService;
import com.festago.bookmark.domain.BookmarkType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@Tag(name = "북마크 등록/삭제 요청 V1")
public class BookmarkManagementV1Controller {

    private final BookmarkFacadeService bookmarkFacadeService;

    @MemberAuth
    @PutMapping
    @Operation(description = "자원의 식별자와 타입으로 북마크를 등록한다.", summary = "북마크 등록")
    public ResponseEntity<Void> putBookmark(
        @Member Long memberId,
        @RequestParam Long resourceId,
        @RequestParam BookmarkType bookmarkType
    ) {
        bookmarkFacadeService.save(bookmarkType, resourceId, memberId);
        return ResponseEntity.ok()
            .build();
    }

    @MemberAuth
    @DeleteMapping
    @Operation(description = "자원의 식별자와 타입으로 북마크를 삭제한다.", summary = "북마크 삭제")
    public ResponseEntity<Void> deleteBookmark(
        @Member Long memberId,
        @RequestParam Long resourceId,
        @RequestParam BookmarkType bookmarkType
    ) {
        bookmarkFacadeService.delete(bookmarkType, resourceId, memberId);
        return ResponseEntity.noContent()
            .build();
    }
}
