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
    private final BookmarkAppendValidator bookmarkAppendValidator;
    private final SchoolRepository schoolRepository;

    public Long save(Long schoolId, Long memberId) {
        validate(schoolId, memberId);
        return bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, memberId))
            .getId();
    }

    private void validate(Long schoolId, Long memberId) {
        if (!schoolRepository.existsById(schoolId)) {
            throw new IllegalArgumentException("존재하지 않는 학교입니다.");
        }
        bookmarkAppendValidator.validate(BookmarkType.SCHOOL, schoolId, memberId);
    }
}
