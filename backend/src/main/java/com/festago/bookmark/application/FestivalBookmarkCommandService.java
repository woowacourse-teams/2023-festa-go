package com.festago.bookmark.application;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalBookmarkCommandService {

    private static final long MAX_FESTIVAL_BOOKMARK_COUNT = 12L;

    private final BookmarkRepository bookmarkRepository;
    private final FestivalRepository festivalRepository;

    public void putFestivalBookmark(Long memberId, Long festivalId) {
        validate(memberId, festivalId);
        if (isExistsBookmark(memberId, festivalId)) {
            return;
        }
        bookmarkRepository.save(new Bookmark(BookmarkType.FESTIVAL, festivalId, memberId));
    }

    private void validate(Long memberId, Long festivalId) {
        if (!festivalRepository.existsById(festivalId)) {
            throw new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND);
        }
        long festivalBookmarkCount = bookmarkRepository.countByMemberIdAndBookmarkType(memberId, BookmarkType.FESTIVAL);
        if (festivalBookmarkCount >= MAX_FESTIVAL_BOOKMARK_COUNT) {
            throw new BadRequestException(ErrorCode.FOR_TEST_ERROR); // TODO 에러 코드 지정할 것
        }
    }

    private boolean isExistsBookmark(Long memberId, Long festivalId) {
        return bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(
            BookmarkType.FESTIVAL,
            memberId,
            festivalId
        );
    }

    public void deleteFestivalBookmark(Long memberId, Long festivalId) {
        bookmarkRepository.deleteByBookmarkTypeAndMemberIdAndResourceId(
            BookmarkType.FESTIVAL,
            memberId,
            festivalId
        );
    }
}
