package com.festago.bookmark.application;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolBookmarkCommandService {

    private static final int BOOKMARK_MAXIMUM_COUNT = 12;

    private final BookmarkRepository bookmarkRepository;
    private final SchoolRepository schoolRepository;

    public Long save(Long schoolId, Long memberId) {
        validate(schoolId, memberId);
        return bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, memberId))
            .getId();
    }

    private void validate(Long schoolId, Long memberId) {
        if (!schoolRepository.existsById(schoolId)) {
            throw new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND);
        }

        if (bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(BookmarkType.SCHOOL, memberId, schoolId)) {
            throw new IllegalArgumentException("이미 저장된 북마크입니다.");
        }

        long bookmarkCount = bookmarkRepository.countByMemberIdAndBookmarkType(memberId, BookmarkType.SCHOOL);
        if (bookmarkCount >= BOOKMARK_MAXIMUM_COUNT) {
            throw new IllegalArgumentException("북마크는 저장 갯수를 초과하였습니다.");
        }
    }

    public void delete(Long schoolId, Long memberId) {
        bookmarkRepository.deleteByBookmarkTypeAndMemberIdAndResourceId(BookmarkType.SCHOOL, memberId, schoolId);
    }
}
