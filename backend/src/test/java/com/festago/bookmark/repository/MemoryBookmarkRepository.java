package com.festago.bookmark.repository;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.support.AbstractMemoryRepository;
import java.util.Objects;
import java.util.Optional;

public class MemoryBookmarkRepository extends AbstractMemoryRepository<Bookmark> implements BookmarkRepository {

    @Override
    public boolean existsByBookmarkTypeAndMemberIdAndResourceId(
        BookmarkType bookmarkType,
        Long memberId,
        Long resourceId
    ) {
        return getByBookmarkTypeAndMemberIdAndResourceId(bookmarkType, memberId, resourceId)
            .isPresent();
    }

    private Optional<Bookmark> getByBookmarkTypeAndMemberIdAndResourceId(
        BookmarkType bookmarkType,
        Long memberId,
        Long resourceId
    ) {
        return memory.values().stream()
            .filter(bookmark -> bookmark.getBookmarkType() == bookmarkType)
            .filter(bookmark -> Objects.equals(bookmark.getMemberId(), memberId))
            .filter(bookmark -> Objects.equals(bookmark.getResourceId(), resourceId))
            .findAny();
    }

    @Override
    public long countByMemberIdAndBookmarkType(Long memberId, BookmarkType bookmarkType) {
        return memory.values().stream()
            .filter(bookmark -> Objects.equals(bookmark.getMemberId(), memberId))
            .filter(bookmark -> bookmark.getBookmarkType() == bookmarkType)
            .count();
    }

    @Override
    public void deleteByBookmarkTypeAndMemberIdAndResourceId(
        BookmarkType bookmarkType,
        Long memberId,
        Long resourceId
    ) {
        getByBookmarkTypeAndMemberIdAndResourceId(bookmarkType, memberId, resourceId)
            .ifPresent(it -> memory.remove(it.getId()));
    }
}
