package com.festago.bookmark.application.command;

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

    public void save(Long festivalId, Long memberId) {
        validate(festivalId, memberId);
        if (isExistsBookmark(festivalId, memberId)) {
            return;
        }
        bookmarkRepository.save(new Bookmark(BookmarkType.FESTIVAL, festivalId, memberId));
    }

    private void validate(Long festivalId, Long memberId) {
        if (!festivalRepository.existsById(festivalId)) {
            throw new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND);
        }
        long festivalBookmarkCount = bookmarkRepository.countByMemberIdAndBookmarkType(memberId, BookmarkType.FESTIVAL);
        if (festivalBookmarkCount >= MAX_FESTIVAL_BOOKMARK_COUNT) {
            throw new BadRequestException(ErrorCode.BOOKMARK_LIMIT_EXCEEDED);
        }
    }

    private boolean isExistsBookmark(Long festivalId, Long memberId) {
        return bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(
            BookmarkType.FESTIVAL,
            memberId,
            festivalId
        );
    }

    public void delete(Long festivalId, Long memberId) {
        bookmarkRepository.deleteByBookmarkTypeAndMemberIdAndResourceId(
            BookmarkType.FESTIVAL,
            memberId,
            festivalId
        );
    }
}
