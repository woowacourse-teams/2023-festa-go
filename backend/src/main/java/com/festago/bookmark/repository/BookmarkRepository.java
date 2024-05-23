package com.festago.bookmark.repository;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import org.springframework.data.repository.Repository;

public interface BookmarkRepository extends Repository<Bookmark, Long> {

    Bookmark save(Bookmark bookmark);

    void deleteById(Long id);

    boolean existsByBookmarkTypeAndMemberIdAndResourceId(BookmarkType bookmarkType, Long memberId, Long resourceId);

    long countByMemberIdAndBookmarkType(Long memberId, BookmarkType bookmarkType);

    void deleteByBookmarkTypeAndMemberIdAndResourceId(
        BookmarkType bookmarkType,
        Long memberId,
        Long resourceId
    );
}
