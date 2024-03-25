package com.festago.bookmark.application;

import static com.festago.bookmark.domain.BookmarkType.SCHOOL;
import static com.festago.common.exception.ErrorCode.SCHOOL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.common.exception.NotFoundException;
import com.festago.member.repository.MemberRepository;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.MemberFixture;
import com.festago.support.SchoolFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolBookmarkCommandIntegrationServiceTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SchoolBookmarkCommandService schoolBookmarkCommandService;

    @MockBean
    BookmarkAppendValidator bookmarkAppendValidator;

    @Test
    void 학교가_존재하지_않으면_예외() {
        // when && then
        assertThatThrownBy(() -> schoolBookmarkCommandService.save(-1L, 1L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(SCHOOL_NOT_FOUND.getMessage());
    }

    @Test
    void 북마크_저장_성공() {
        // given
        Long memberId = memberRepository.save(MemberFixture.member().build()).getId();
        Long schoolId = schoolRepository.save(SchoolFixture.school().build()).getId();

        // when
        Long actual = schoolBookmarkCommandService.save(schoolId, memberId);

        // then
        assertThat(actual).isPositive();
        verify(bookmarkAppendValidator, only()).validate(SCHOOL, schoolId, memberId);
    }

    @Test
    void 북마크를_삭제한다() {
        // given
        Long memberId = memberRepository.save(MemberFixture.member().build()).getId();
        Long schoolId = schoolRepository.save(SchoolFixture.school().build()).getId();
        bookmarkRepository.save(new Bookmark(SCHOOL, schoolId, memberId));

        // when
        schoolBookmarkCommandService.delete(schoolId, memberId);

        // then
        var actual = bookmarkRepository.existsByBookmarkTypeAndMemberIdAndResourceId(SCHOOL, memberId,
            schoolId);
        assertThat(actual).isFalse();
    }
}
