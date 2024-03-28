package com.festago.bookmark.application;

import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchoolBookmarkAppendValidator {

    private static final int BOOKMARK_MAXIMUM_COUNT = 12;
    private final BookmarkRepository bookmarkRepository;

    //TODO: 커스텀 예외 적용
    public void validate(Long schoolId, Long memberId) {
        long bookmarkCount = bookmarkRepository.countByMemberIdAndBookmarkType(memberId, BookmarkType.SCHOOL);
        if (bookmarkCount >= BOOKMARK_MAXIMUM_COUNT) {
            throw new IllegalArgumentException("북마크는 저장 갯수를 초과하였습니다.");
        }

        if (bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(BookmarkType.SCHOOL, memberId, schoolId)) {
            throw new IllegalArgumentException("이미 저장된 북마크입니다.");
        }
    }
}
