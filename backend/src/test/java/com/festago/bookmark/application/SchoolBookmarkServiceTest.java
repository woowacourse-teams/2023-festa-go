package com.festago.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolBookmarkServiceTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    SchoolBookmarkCommandService schoolBookmarkCommandService;

    Long 회원_Id = 1L;
    School 학교;

    @BeforeEach
    void setUp() {
        학교 = schoolRepository.save(new School("knu.ac.kr", "경북대학교", SchoolRegion.대구));
    }

    @Test
    void 학교가_존재하지_않으면_예외() {
        // when && then
        assertThatThrownBy(() -> schoolBookmarkCommandService.save(-1L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 학교입니다.");
    }

    @Test
    void 북마크_갯수가_12개_이상이면_예외() {
        // given
        for (int i = 0; i < 12; i++) {
            School school = schoolRepository.save(new School(i + "domain.ac.kr", "school" + i, SchoolRegion.ANY));
            bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, school.getId(), 회원_Id));
        }

        // when && then
        assertThatThrownBy(() -> schoolBookmarkCommandService.save(학교.getId(), 회원_Id))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("북마크는 최대 12개까지 가능합니다.");
    }

    @Test
    void 이미_해당하는_북마크가_저장됐다면_예외() {
        // given
        bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, 학교.getId(), 회원_Id));

        // when && then
        assertThatThrownBy(() -> schoolBookmarkCommandService.save(학교.getId(), 회원_Id))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 저장된 북마크입니다.");
    }

    @Test
    void 북마크_저장_성공() {
        // when
        Long actual = schoolBookmarkCommandService.save(학교.getId(), 회원_Id);

        // then
        assertThat(actual).isPositive();
    }
}
