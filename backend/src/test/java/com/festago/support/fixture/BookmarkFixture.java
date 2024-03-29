package com.festago.support.fixture;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;

public class BookmarkFixture extends BaseFixture {

    private Long id;
    private BookmarkType bookmarkType;
    private Long resourceId;
    private Long memberId;

    public static BookmarkFixture builder() {
        return new BookmarkFixture();
    }

    public BookmarkFixture id(Long id) {
        this.id = id;
        return this;
    }

    public BookmarkFixture bookmarkType(BookmarkType bookmarkType) {
        this.bookmarkType = bookmarkType;
        return this;
    }

    public BookmarkFixture resourceId(Long resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public BookmarkFixture memberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public Bookmark build() {
        return new Bookmark(
            id,
            bookmarkType,
            resourceId,
            memberId
        );
    }
}
