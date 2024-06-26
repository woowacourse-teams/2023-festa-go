package com.festago.bookmark.application.command;

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

    private static final long MAX_ARTIST_BOOKMARK_COUNT = 12L;

    private final BookmarkRepository bookmarkRepository;
    private final ArtistRepository artistRepository;

    public void save(Long artistId, Long memberId) {
        validate(artistId, memberId);
        if (isExistsBookmark(artistId, memberId)) {
            return;
        }
        bookmarkRepository.save(new Bookmark(BookmarkType.ARTIST, artistId, memberId));
    }

    private void validate(Long artistId, Long memberId) {
        validateExistArtist(artistId);
        validateMaxBookmark(memberId);
    }

    private void validateExistArtist(Long artistId) {
        if (!artistRepository.existsById(artistId)) {
            throw new NotFoundException(ErrorCode.ARTIST_NOT_FOUND);
        }
    }

    private void validateMaxBookmark(Long memberId) {
        long bookmarkCount = bookmarkRepository.countByMemberIdAndBookmarkType(memberId, BookmarkType.ARTIST);
        if (bookmarkCount >= MAX_ARTIST_BOOKMARK_COUNT) {
            throw new BadRequestException(ErrorCode.BOOKMARK_LIMIT_EXCEEDED);
        }
    }

    private boolean isExistsBookmark(Long artistId, Long memberId) {
        return bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(
            BookmarkType.ARTIST,
            memberId,
            artistId
        );
    }

    public void delete(Long artistId, Long memberId) {
        bookmarkRepository.deleteByBookmarkTypeAndMemberIdAndResourceId(BookmarkType.ARTIST, memberId, artistId);
    }
}
