package com.festago.bookmark.application;

import static com.festago.bookmark.domain.BookmarkType.SCHOOL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.bookmark.repository.MemoryBookmarkRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.repository.MemorySchoolRepository;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.fixture.SchoolFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolBookmarkCommandServiceTest {

    BookmarkRepository bookmarkRepository;
    SchoolRepository schoolRepository;
    SchoolBookmarkCommandService schoolBookmarkCommandService;

    @BeforeEach
    void setUp() {
        bookmarkRepository = new MemoryBookmarkRepository();
        schoolRepository = new MemorySchoolRepository();
        schoolBookmarkCommandService = new SchoolBookmarkCommandService(bookmarkRepository, schoolRepository);
    }

    @Nested
    class 학교_북마크_추가시 {

        @Test
        void 해당하는_학교가_없으면_예외() {
            // when && then
            assertThatThrownBy(() -> schoolBookmarkCommandService.save(-1L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.SCHOOL_NOT_FOUND.getMessage());
        }

        @Test
        void 학교_북마크_갯수가_이미_12개_이상이면_예외() {
            // given
            Long memberId = 1L;
            for (long i = 0; i < 12; i++) {
                Long schoolId = schoolRepository.save(SchoolFixture.builder().build()).getId();
                bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, memberId));
            }

            Long schoolId = schoolRepository.save(SchoolFixture.builder().build()).getId();

            // when && then
            assertThatThrownBy(() -> schoolBookmarkCommandService.save(schoolId, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("북마크는 저장 갯수를 초과하였습니다.");
        }

        @Test
        void 이미_해당하는_북마크가_저장됐다면_예외() {
            // given
            Long schoolId = schoolRepository.save(SchoolFixture.builder().build()).getId();
            bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, schoolId, 1L));

            // when && then
            assertThatThrownBy(() -> schoolBookmarkCommandService.save(schoolId, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 저장된 북마크입니다.");
        }

        @Test
        void 북마크_저장_성공() {
            // given
            Long memberId = 1L;
            Long schoolId = schoolRepository.save(SchoolFixture.builder().build()).getId();

            // when
            Long actual = schoolBookmarkCommandService.save(schoolId, memberId);

            // then
            assertThat(actual).isPositive();
        }
    }

    @Test
    void 북마크를_삭제한다() {
        // given
        Long memberId = 1L;
        Long schoolId = schoolRepository.save(SchoolFixture.builder().build()).getId();
        bookmarkRepository.save(new Bookmark(SCHOOL, schoolId, memberId));

        // when
        schoolBookmarkCommandService.delete(schoolId, memberId);

        // then
        var actual = bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(SCHOOL, memberId,
            schoolId);
        assertThat(actual).isFalse();
    }
}
