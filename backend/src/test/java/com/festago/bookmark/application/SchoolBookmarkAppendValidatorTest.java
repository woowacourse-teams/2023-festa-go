package com.festago.bookmark.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.MemoryBookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolBookmarkAppendValidatorTest {

    MemoryBookmarkRepository bookmarkRepository = new MemoryBookmarkRepository();
    SchoolBookmarkAppendValidator schoolBookmarkAppendValidator = new SchoolBookmarkAppendValidator(bookmarkRepository);

    @BeforeEach
    void setUp() {
        bookmarkRepository.clear();
    }

    @Test
    void 학교_북마크_갯수가_이미_12개_이상이면_예외() {
        // given
        Long memberId = 1L;
        for (long i = 0; i < 12; i++) {
            bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, i + 1, memberId));
        }

        // when && then
        assertThatThrownBy(() -> schoolBookmarkAppendValidator.validate(13L, memberId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("북마크는 최대 12개까지 가능합니다.");
    }

    @Test
    void 이미_해당하는_북마크가_저장됐다면_예외() {
        // given
        bookmarkRepository.save(new Bookmark(BookmarkType.SCHOOL, 1L, 1L));

        // when && then
        assertThatThrownBy(() -> schoolBookmarkAppendValidator.validate( 1L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 저장된 북마크입니다.");
    }
}
