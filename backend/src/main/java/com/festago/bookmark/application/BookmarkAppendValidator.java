package com.festago.bookmark.application;

import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkAppendValidator {

    private final BookmarkRepository bookmarkRepository;

    public void validate(BookmarkType bookmarkType, Long resourceId, Long memberId) {
        long bookmarkCount = bookmarkRepository.countByMemberIdAndBookmarkType(memberId, bookmarkType);
        if (bookmarkCount >= 12) {
            throw new IllegalArgumentException("북마크는 최대 12개까지 가능합니다.");
        }

        if (bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(bookmarkType, memberId, resourceId)) {
            throw new IllegalArgumentException("이미 저장된 북마크입니다.");
        }
    }
}
