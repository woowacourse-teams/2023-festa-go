package com.festago.bookmark.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.bookmark.repository.MemoryBookmarkRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.domain.School;
import com.festago.school.repository.MemorySchoolRepository;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.fixture.BookmarkFixture;
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

    Long 회원_식별자 = 1234L;

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
            assertThatThrownBy(() -> schoolBookmarkCommandService.save(-1L, 회원_식별자))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.SCHOOL_NOT_FOUND.getMessage());
        }

        @Test
        void 학교_북마크_갯수가_이미_12개_이상이면_예외() {
            // given
            for (long i = 0; i < 12; i++) {
                School school = schoolRepository.save(SchoolFixture.builder().build());
                bookmarkRepository.save(
                    BookmarkFixture.builder()
                        .bookmarkType(BookmarkType.SCHOOL)
                        .resourceId(school.getId())
                        .memberId(회원_식별자)
                        .build()
                );
            }

            Long schoolId = schoolRepository.save(SchoolFixture.builder().build()).getId();

            // when && then
            assertThatThrownBy(() -> schoolBookmarkCommandService.save(schoolId, 회원_식별자))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.BOOKMARK_LIMIT_EXCEEDED.getMessage());
        }

        @Test
        void 이미_해당하는_북마크가_저장됐다면_예외() {
            // given
            Long schoolId = schoolRepository.save(SchoolFixture.builder().build()).getId();
            bookmarkRepository.save(
                BookmarkFixture.builder()
                    .bookmarkType(BookmarkType.SCHOOL)
                    .resourceId(schoolId)
                    .memberId(회원_식별자)
                    .build()
            );

            // when
            schoolBookmarkCommandService.save(schoolId, 회원_식별자);

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.SCHOOL))
                .isOne();
        }

        @Test
        void 북마크_저장_성공() {
            // given
            Long schoolId = schoolRepository.save(SchoolFixture.builder().build()).getId();

            // when
            schoolBookmarkCommandService.save(schoolId, 회원_식별자);

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.SCHOOL))
                .isNotZero();
        }
    }

    @Test
    void 북마크를_삭제한다() {
        // given
        Long schoolId = schoolRepository.save(SchoolFixture.builder().build()).getId();
        bookmarkRepository.save(
            BookmarkFixture.builder()
                .bookmarkType(BookmarkType.SCHOOL)
                .resourceId(schoolId)
                .memberId(회원_식별자)
                .build()
        );

        // when
        schoolBookmarkCommandService.delete(schoolId, 회원_식별자);

        // then
        assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.SCHOOL))
            .isZero();
    }
}
