package com.festago.bookmark.application;

import com.festago.bookmark.domain.BookmarkType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkFacadeService {

    private final ArtistBookmarkCommandService artistBookmarkCommandService;
    private final FestivalBookmarkCommandService festivalBookmarkCommandService;
    private final SchoolBookmarkCommandService schoolBookmarkCommandService;

    public void save(
        BookmarkType bookmarkType,
        Long resourceId,
        Long memberId
    ) {
        switch (bookmarkType) {
            case SCHOOL -> schoolBookmarkCommandService.save(resourceId, memberId);
            case ARTIST -> artistBookmarkCommandService.save(resourceId, memberId);
            case FESTIVAL -> festivalBookmarkCommandService.putFestivalBookmark(memberId, resourceId);
        }
    }

    public void delete(
        BookmarkType bookmarkType,
        Long resourceId,
        Long memberId
    ) {
        switch (bookmarkType) {
            case SCHOOL -> schoolBookmarkCommandService.delete(resourceId, memberId);
            case ARTIST -> artistBookmarkCommandService.delete(resourceId, memberId);
            case FESTIVAL -> festivalBookmarkCommandService.deleteFestivalBookmark(memberId, resourceId);
        }
    }
}
