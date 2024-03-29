package com.festago.bookmark.application;

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

    Long 회원_식별자 = 1L;
    Festival 축제;

    @BeforeEach
    void setUp() {
        bookmarkRepository = new MemoryBookmarkRepository();
        festivalRepository = new MemoryFestivalRepository();
        festivalBookmarkCommandService = new FestivalBookmarkCommandService(bookmarkRepository, festivalRepository);
        축제 = festivalRepository.save(FestivalFixture.builder().build());
    }

    @Nested
    class putFestivalBookmark {

        @Test
        void 북마크로_저장하려는_축제가_존재하지_않으면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> festivalBookmarkCommandService.putFestivalBookmark(회원_식별자, 4885L))
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
            assertThatThrownBy(() -> festivalBookmarkCommandService.putFestivalBookmark(회원_식별자, 축제.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.FOR_TEST_ERROR.getMessage());
        }

        @Test
        void 북마크를_저장한다() {
            // when
            festivalBookmarkCommandService.putFestivalBookmark(회원_식별자, 축제.getId());

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.FESTIVAL))
                .isNotZero();
        }

        @Test
        void 기존에_저장된_북마크가_있으면_저장되지_않는다() {
            // given
            festivalBookmarkCommandService.putFestivalBookmark(회원_식별자, 축제.getId());

            // when
            festivalBookmarkCommandService.putFestivalBookmark(회원_식별자, 축제.getId());

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.FESTIVAL))
                .isOne();
        }
    }

    @Nested
    class deleteFestivalBookmark {

        @Test
        void 존재하지_않는_북마크를_삭제하더라도_예외가_발생하지_않는다() {
            // when & then
            assertThatNoException()
                .isThrownBy(() -> festivalBookmarkCommandService.deleteFestivalBookmark(회원_식별자, 4885L));
        }

        @Test
        void 북마크를_삭제할_수_있다() {
            // given
            festivalBookmarkCommandService.putFestivalBookmark(회원_식별자, 축제.getId());

            // when
            festivalBookmarkCommandService.deleteFestivalBookmark(회원_식별자, 축제.getId());

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.FESTIVAL))
                .isZero();
        }
    }
}
