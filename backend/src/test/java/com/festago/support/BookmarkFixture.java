package com.festago.support;

import com.festago.bookmark.domain.BookmarkType;
import lombok.Builder;

@Builder
public class BookmarkFixture {

    private Long id;
    private BookmarkType bookmarkType;
    private Long resourceId;
    private Long memberId;

}
