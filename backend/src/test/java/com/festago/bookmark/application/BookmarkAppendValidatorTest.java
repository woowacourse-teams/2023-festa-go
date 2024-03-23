package com.festago.bookmark.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.MemoryBookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BookmarkAppendValidatorTest {

    MemoryBookmarkRepository bookmarkRepository = new MemoryBookmarkRepository();
    BookmarkAppendValidator bookmarkAppendValidator = new BookmarkAppendValidator(bookmarkRepository);

    @BeforeEach
    void setUp() {
        bookmarkRepository.clear();
    }

    @ParameterizedTest
    @EnumSource(value = BookmarkType.class)
    void 같은_타입에_북마크_갯수가_이미_12개_이상이면_예외(BookmarkType bookmarkType) {
        // given
        Long memberId = 1L;
        for (int i = 0; i < 12; i++) {
            bookmarkRepository.save(new Bookmark(bookmarkType, (long) i + 1, memberId));
        }

        // when && then
        assertThatThrownBy(() -> bookmarkAppendValidator.validate(bookmarkType, 13L, memberId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("북마크는 최대 12개까지 가능합니다.");
    }

    @ParameterizedTest
    @EnumSource(value = BookmarkType.class)
    void 이미_해당하는_북마크가_저장됐다면_예외(BookmarkType bookmarkType) {
        // given
        bookmarkRepository.save(new Bookmark(bookmarkType, 1L,1L));

        // when && then
        assertThatThrownBy(() -> bookmarkAppendValidator.validate(bookmarkType, 1L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 저장된 북마크입니다.");
    }
}
