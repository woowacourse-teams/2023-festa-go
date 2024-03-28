package com.festago.bookmark.application;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolBookmarkCommandService {

    private final BookmarkRepository bookmarkRepository;
    private final SchoolBookmarkAppendValidator schoolBookmarkAppendValidator;

    public Long save(Long schoolId, Long memberId) {
        schoolBookmarkAppendValidator.validate(schoolId, memberId);
        return bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, memberId))
            .getId();
    }

    public void delete(Long schoolId, Long memberId) {
        bookmarkRepository.deleteByBookmarkTypeAndMemberIdAndResourceId(BookmarkType.SCHOOL, memberId, schoolId);
    }
}
