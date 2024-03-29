package com.festago.bookmark.application.integration;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.bookmark.application.ArtistBookmarkV1QueryService;
import com.festago.bookmark.domain.BookmarkType;
import com.festago.bookmark.dto.v1.ArtistBookmarkV1Response;
import com.festago.bookmark.repository.BookmarkRepository;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.BookmarkFixture;
import com.festago.support.fixture.MemberFixture;
import com.festago.support.fixture.SchoolFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistBookmarkV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    ArtistBookmarkV1QueryService artistBookmarkV1QueryService;

    Artist 네오;
    Artist 브라운;
    Artist 브리;

    Member 푸우;
    Member 오리;
    Member 글렌;

    @BeforeEach
    void setting() {
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

    @Test
    void 유저의_아티스트_북마크_목록을_반환한다() {
        // given
        createBookmark(브리.getId(), 푸우.getId());
        createBookmark(네오.getId(), 푸우.getId());
        createBookmark(브라운.getId(), 오리.getId());

        // when
        List<ArtistBookmarkV1Response> actual = artistBookmarkV1QueryService.findArtistBookmarksByMemberId(
            푸우.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(2);
            softly.assertThat(actual).extracting("artistInfo").extracting("name").contains("브리", "네오");
        });
    }

    @Test
    void 북마크들_중_아티스트에_대한_북마크만_가져온다() {
        // given
        createBookmark(브리.getId(), 푸우.getId());
        createBookmark(네오.getId(), 푸우.getId());
        School school = schoolRepository.save(SchoolFixture.builder().build());
        bookmarkRepository.save(
            BookmarkFixture.builder()
                .bookmarkType(BookmarkType.SCHOOL)
                .resourceId(school.getId())
                .memberId(푸우.getId())
                .build()
        );

        // when
        List<ArtistBookmarkV1Response> actual = artistBookmarkV1QueryService.findArtistBookmarksByMemberId(
            푸우.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(2);
            softly.assertThat(actual).extracting("artistInfo").extracting("name").contains("브리", "네오");
        });
    }

    public void createBookmark(Long resourceId, Long memberId) {
        bookmarkRepository.save(BookmarkFixture.builder()
            .bookmarkType(BookmarkType.ARTIST)
            .resourceId(resourceId)
            .memberId(memberId)
            .build()
        );
    }
}
