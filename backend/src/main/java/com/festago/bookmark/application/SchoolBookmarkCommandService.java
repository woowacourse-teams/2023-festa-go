package com.festago.bookmark.application;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolBookmarkCommandService {

    private final BookmarkRepository bookmarkRepository;
    private final SchoolRepository schoolRepository;

    public Long save(Long schoolId, Long memberId) {
        if (!schoolRepository.existsById(schoolId)) {
            throw new IllegalArgumentException("존재하지 않는 학교입니다.");
        }

        long bookmarkCount = bookmarkRepository.countByMemberIdAndBookmarkType(memberId, BookmarkType.SCHOOL);
        if (bookmarkCount >= 12) {
            throw new IllegalArgumentException("북마크는 최대 12개까지 가능합니다.");
        }

        if (bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(BookmarkType.SCHOOL, memberId, schoolId)) {
            throw new IllegalArgumentException("이미 저장된 북마크입니다.");
        }
        return bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, memberId))
            .getId();
    }
}
