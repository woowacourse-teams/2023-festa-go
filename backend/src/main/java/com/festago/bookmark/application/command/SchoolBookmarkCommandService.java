package com.festago.bookmark.application.command;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.common.exception.BadRequestException;
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

    private static final long MAX_SCHOOL_BOOKMARK_COUNT = 12L;

    private final BookmarkRepository bookmarkRepository;
    private final SchoolRepository schoolRepository;

    public void save(Long schoolId, Long memberId) {
        validate(schoolId, memberId);
        if (isExistsBookmark(schoolId, memberId)) {
            return;
        }
        bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, memberId));
    }

    private void validate(Long schoolId, Long memberId) {
        if (!schoolRepository.existsById(schoolId)) {
            throw new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND);
        }

        long bookmarkCount = bookmarkRepository.countByMemberIdAndBookmarkType(memberId, BookmarkType.SCHOOL);
        if (bookmarkCount >= MAX_SCHOOL_BOOKMARK_COUNT) {
            throw new BadRequestException(ErrorCode.BOOKMARK_LIMIT_EXCEEDED);
        }
    }

    private boolean isExistsBookmark(Long schoolId, Long memberId) {
        return bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(
            BookmarkType.SCHOOL,
            memberId,
            schoolId
        );
    }

    public void delete(Long schoolId, Long memberId) {
        bookmarkRepository.deleteByBookmarkTypeAndMemberIdAndResourceId(BookmarkType.SCHOOL, memberId, schoolId);
    }
}
