package com.festago.bookmark.application;

import com.festago.artist.repository.ArtistRepository;
import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistBookmarkCommandService {

    private static final int MAX_ARTIST_BOOKMARK_COUNT = 12;
    private final BookmarkRepository bookmarkRepository;
    private final ArtistRepository artistRepository;

    public Long save(Long artistId, Long memberId) {
        validate(artistId, memberId);
        return bookmarkRepository.save(new Bookmark(BookmarkType.ARTIST, artistId, memberId))
            .getId();
    }

    private void validate(Long artistId, Long memberId) {
        validateExistArtist(artistId);
        validateMaxBookmark(memberId);
    }

    private void validateExistArtist(Long artistId) {
        if (artistRepository.findById(artistId).isEmpty()) {
            throw new NotFoundException(ErrorCode.ARTIST_NOT_FOUND);
        }
    }

    private void validateMaxBookmark(Long memberId) {
        long bookmarkCount = bookmarkRepository.countByMemberIdAndBookmarkType(memberId, BookmarkType.ARTIST);
        if (bookmarkCount >= MAX_ARTIST_BOOKMARK_COUNT) {
            throw new BadRequestException(ErrorCode.BOOKMARK_LIMIT_EXCEEDED);
        }
    }

    public void delete(Long memberId, Long artistId) {
        bookmarkRepository.deleteByBookmarkTypeAndMemberIdAndResourceId(BookmarkType.ARTIST, memberId, artistId);
    }
}
