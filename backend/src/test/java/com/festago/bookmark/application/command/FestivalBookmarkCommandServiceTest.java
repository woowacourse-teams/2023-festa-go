package com.festago.bookmark.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.bookmark.repository.MemoryBookmarkRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.festival.repository.MemoryFestivalRepository;
import com.festago.support.fixture.FestivalFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalBookmarkCommandServiceTest {

    BookmarkRepository bookmarkRepository;

    FestivalRepository festivalRepository;

    FestivalBookmarkCommandService festivalBookmarkCommandService;

    Long 회원_식별자 = 1234L;
    Festival 축제;

    @BeforeEach
    void setUp() {
        bookmarkRepository = new MemoryBookmarkRepository();
        festivalRepository = new MemoryFestivalRepository();
        festivalBookmarkCommandService = new FestivalBookmarkCommandService(bookmarkRepository, festivalRepository);
        축제 = festivalRepository.save(FestivalFixture.builder().build());
    }

    @Nested
    class 북마크_저장 {

        @Test
        void 북마크로_저장하려는_축제가_존재하지_않으면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> festivalBookmarkCommandService.save(4885L, 회원_식별자))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.FESTIVAL_NOT_FOUND.getMessage());
        }

        @Test
        void 기존에_저장된_북마크의_개수가_12개_이상이면_예외가_발생한다() {
            // given
            for (long i = 1; i <= 12; i++) {
                Festival festival = festivalRepository.save(FestivalFixture.builder().build());
                bookmarkRepository.save(new Bookmark(BookmarkType.FESTIVAL, festival.getId(), 회원_식별자));
            }

            // when & then
            assertThatThrownBy(() -> festivalBookmarkCommandService.save(축제.getId(), 회원_식별자))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.BOOKMARK_LIMIT_EXCEEDED.getMessage());
        }

        @Test
        void 북마크를_저장한다() {
            // when
            festivalBookmarkCommandService.save(축제.getId(), 회원_식별자);

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.FESTIVAL))
                .isNotZero();
        }

        @Test
        void 기존에_저장된_북마크가_있으면_저장되지_않는다() {
            // given
            festivalBookmarkCommandService.save(축제.getId(), 회원_식별자);

            // when
            festivalBookmarkCommandService.save(축제.getId(), 회원_식별자);

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.FESTIVAL))
                .isOne();
        }
    }

    @Nested
    class 북마크_삭제 {

        @Test
        void 존재하지_않는_북마크를_삭제하더라도_예외가_발생하지_않는다() {
            // when & then
            assertThatNoException()
                .isThrownBy(() -> festivalBookmarkCommandService.delete(4885L, 회원_식별자));
        }

        @Test
        void 북마크를_삭제할_수_있다() {
            // given
            festivalBookmarkCommandService.save(축제.getId(), 회원_식별자);

            // when
            festivalBookmarkCommandService.delete(축제.getId(), 회원_식별자);

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.FESTIVAL))
                .isZero();
        }
    }
}
