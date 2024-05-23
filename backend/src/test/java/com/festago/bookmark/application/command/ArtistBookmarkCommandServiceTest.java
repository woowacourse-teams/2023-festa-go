package com.festago.bookmark.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.bookmark.repository.MemoryBookmarkRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.BookmarkFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistBookmarkCommandServiceTest {

    ArtistRepository artistRepository;
    BookmarkRepository bookmarkRepository;
    ArtistBookmarkCommandService artistBookmarkCommandService;

    Long 회원_식별자 = 1234L;
    Artist 브라운;

    @BeforeEach
    void setting() {
        initializeRepository();
        브라운 = artistRepository.save(ArtistFixture.builder()
            .name("브라운")
            .build());
    }

    private void initializeRepository() {
        artistRepository = new MemoryArtistRepository();
        bookmarkRepository = new MemoryBookmarkRepository();
        artistBookmarkCommandService = new ArtistBookmarkCommandService(bookmarkRepository, artistRepository);
    }

    @Nested
    class 북마크_저장 {

        @Test
        void 존재_하지_않는_아티스트로_저장하면_예외가_발생한다() {
            // given & when & then
            assertThatThrownBy(() -> artistBookmarkCommandService.save(1000L, 회원_식별자))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ARTIST_NOT_FOUND.getMessage());
        }

        @Test
        void 최대_북마크_개수를_넘기면_에외가_발생한다() {
            // given
            for (int i = 0; i < 12; i++) {
                Artist artist = artistRepository.save(ArtistFixture.builder().build());
                bookmarkRepository.save(
                    BookmarkFixture.builder()
                        .bookmarkType(BookmarkType.ARTIST)
                        .resourceId(artist.getId())
                        .memberId(회원_식별자)
                        .build()
                );
            }

            // when & then
            assertThatThrownBy(() -> artistBookmarkCommandService.save(브라운.getId(), 회원_식별자))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.BOOKMARK_LIMIT_EXCEEDED.getMessage());
        }

        @Test
        void 북마크를_저장한다() {
            // when
            artistBookmarkCommandService.save(브라운.getId(), 회원_식별자);

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.ARTIST))
                .isNotZero();
        }

        @Test
        void 기존에_저장된_북마크가_있으면_저장되지_않는다() {
            // given
            artistBookmarkCommandService.save(브라운.getId(), 회원_식별자);

            // when
            artistBookmarkCommandService.save(브라운.getId(), 회원_식별자);

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.ARTIST))
                .isOne();
        }
    }

    @Nested
    class 북마크_삭제 {

        @Test
        void 북마크를_삭제한다() {
            // given
            bookmarkRepository.save(BookmarkFixture.builder()
                .bookmarkType(BookmarkType.ARTIST)
                .resourceId(브라운.getId())
                .memberId(회원_식별자)
                .build());

            // when
            artistBookmarkCommandService.delete(브라운.getId(), 회원_식별자);

            // then
            assertThat(bookmarkRepository.countByMemberIdAndBookmarkType(회원_식별자, BookmarkType.ARTIST))
                .isZero();
        }
    }
}
