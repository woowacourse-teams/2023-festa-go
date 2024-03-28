package com.festago.bookmark.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.bookmark.repository.MemoryBookmarkRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.repository.MemorySchoolRepository;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.SchoolFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolBookmarkAppendValidatorTest {

    BookmarkRepository bookmarkRepository;
    SchoolRepository schoolRepository;
    SchoolBookmarkAppendValidator schoolBookmarkAppendValidator;

    @BeforeEach
    void setUp() {
        bookmarkRepository = new MemoryBookmarkRepository();
        schoolRepository = new MemorySchoolRepository();
        schoolBookmarkAppendValidator = new SchoolBookmarkAppendValidator(bookmarkRepository, schoolRepository);
    }

    @Test
    void 해당하는_학교가_없으면_예외() {
        // when && then
        assertThatThrownBy(() -> schoolBookmarkAppendValidator.validate(-1L, 1L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorCode.SCHOOL_NOT_FOUND.getMessage());
    }

    @Test
    void 학교_북마크_갯수가_이미_12개_이상이면_예외() {
        // given
        Long memberId = 1L;
        for (long i = 0; i < 12; i++) {
            Long schoolId = schoolRepository.save(SchoolFixture.school().build()).getId();
            bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, memberId));
        }

        Long schoolId = schoolRepository.save(SchoolFixture.school().build()).getId();

        // when && then
        assertThatThrownBy(() -> schoolBookmarkAppendValidator.validate(schoolId, memberId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("북마크는 저장 갯수를 초과하였습니다.");
    }

    @Test
    void 이미_해당하는_북마크가_저장됐다면_예외() {
        // given
        Long schoolId = schoolRepository.save(SchoolFixture.school().build()).getId();
        bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, 1L));

        // when && then
        assertThatThrownBy(() -> schoolBookmarkAppendValidator.validate(schoolId, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 저장된 북마크입니다.");
    }
}
