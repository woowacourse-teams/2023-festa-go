package com.festago.bookmark.repository;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryBookmarkRepository implements BookmarkRepository {

    private final ConcurrentHashMap<Long, Bookmark> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    public void clear() {
        memory.clear();
    }

    @Override
    @SneakyThrows
    public Bookmark save(Bookmark bookmark) {
        Field idField = bookmark.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(bookmark, autoIncrement.incrementAndGet());
        memory.put(bookmark.getId(), bookmark);
        return bookmark;
    }

    @Override
    public void deleteById(Long id) {
        memory.remove(id);
    }

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
