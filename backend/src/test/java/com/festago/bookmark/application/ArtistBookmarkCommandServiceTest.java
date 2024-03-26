package com.festago.bookmark.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.artist.repository.MemoryArtistRepository;
import com.festago.bookmark.domain.Bookmark;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.bookmark.repository.MemoryBookmarkRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.member.repository.MemoryMemberRepository;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistBookmarkCommandServiceTest {

    ArtistRepository artistRepository;
    MemberRepository memberRepository;
    BookmarkRepository bookmarkRepository;
    ArtistBookmarkCommandService artistBookmarkCommandService;

    Artist 브라운;
    Artist 브리;
    Artist 네오;

    Member 푸우;
    Member 오리;
    Member 글렌;

    @BeforeEach
    void setting() {
        initializeRepository();
        네오 = artistRepository.save(ArtistFixture.builder()
            .name("네오")
            .build());
        브라운 = artistRepository.save(ArtistFixture.builder()
            .name("브라운")
            .build());
        브리 = artistRepository.save(ArtistFixture.builder()
            .name("브리")
            .build());

        푸우 = memberRepository.save(MemberFixture.builder()
            .nickname("푸우")
            .build());
        오리 = memberRepository.save(MemberFixture.builder()
            .nickname("오리")
            .build());
        글렌 = memberRepository.save(MemberFixture.builder()
            .nickname("글렌")
            .build());
    }

    private void initializeRepository() {
        artistRepository = new MemoryArtistRepository();
        memberRepository = new MemoryMemberRepository();
        bookmarkRepository = new MemoryBookmarkRepository();
        artistBookmarkCommandService = new ArtistBookmarkCommandService(bookmarkRepository, artistRepository);
    }

    @Nested
    class 아티스트_북마크_저장에서 {

        @Test
        void 존재_하지_않는_아티스트로_저장하면_예외가_발생한다() {
            // given & when & then
            assertThatThrownBy(() -> artistBookmarkCommandService.save(1000L, 푸우.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ARTIST_NOT_FOUND.getMessage());
        }

        @Test
        void 최대_북마크_개수를_넘기면_에외가_발생한다() {
            // given
            for (int i = 0; i < 12; i++) {
                bookmarkRepository.save(new Bookmark(BookmarkType.ARTIST, i + 10L, 푸우.getId()));
            }

            // when & then
            assertThatThrownBy(() -> artistBookmarkCommandService.save(브리.getId(), 푸우.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.BOOKMARK_LIMIT_EXCEEDED.getMessage());
        }
    }
}
