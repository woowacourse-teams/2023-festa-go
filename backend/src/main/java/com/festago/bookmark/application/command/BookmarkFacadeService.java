package com.festago.bookmark.application.command;

import com.festago.bookmark.domain.BookmarkType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkFacadeService {

    private final SchoolBookmarkCommandService schoolBookmarkCommandService;
    private final ArtistBookmarkCommandService artistBookmarkCommandService;
    private final FestivalBookmarkCommandService festivalBookmarkCommandService;

    public void save(
        BookmarkType bookmarkType,
        Long resourceId,
        Long memberId
    ) {
        switch (bookmarkType) {
            case SCHOOL -> schoolBookmarkCommandService.save(resourceId, memberId);
            case ARTIST -> artistBookmarkCommandService.save(resourceId, memberId);
            case FESTIVAL -> festivalBookmarkCommandService.save(resourceId, memberId);
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
            case FESTIVAL -> festivalBookmarkCommandService.delete(resourceId, memberId);
        }
    }
}
