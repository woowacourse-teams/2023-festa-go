package com.festago.admin.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.admin.application.AdminArtistV1QueryService;
import com.festago.admin.dto.artist.AdminArtistV1Response;
import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.querydsl.SearchCondition;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.ArtistFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminArtistV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    AdminArtistV1QueryService adminArtistV1QueryService;

    @Autowired
    ArtistRepository artistRepository;

    @Test
    void 아티스트를_단건_조회한다() {
        // given
        Artist expected = artistRepository.save(ArtistFixture.builder().build());

        // when
        var actual = adminArtistV1QueryService.findById(expected.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.id()).isEqualTo(expected.getId());
            softly.assertThat(actual.name()).isEqualTo(expected.getName());
            softly.assertThat(actual.profileImage()).isEqualTo(expected.getProfileImage());
        });
    }

    @Nested
    class findAll {

        Artist 벤;
        Artist 베토벤;
        Artist 아이유;
        Artist 에픽하이;
        Artist 소녀시대;

        @BeforeEach
        void setUp() {
            벤 = artistRepository.save(ArtistFixture.builder()
                .name("벤")
                .build());
            베토벤 = artistRepository.save(ArtistFixture.builder()
                .name("베토벤")
                .build());
            아이유 = artistRepository.save(ArtistFixture.builder()
                .name("아이유")
                .build());
            에픽하이 = artistRepository.save(ArtistFixture.builder()
                .name("에픽하이")
                .build());
            소녀시대 = artistRepository.save(ArtistFixture.builder()
                .name("소녀시대")
                .build());
        }

        @Test
        void 정렬이_되어야_한다() {
            // given
            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
            SearchCondition searchCondition = new SearchCondition("", "", pageable);

            // when
            var response = adminArtistV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .map(AdminArtistV1Response::name)
                .containsExactly(베토벤.getName(), 벤.getName(), 소녀시대.getName(), 아이유.getName(), 에픽하이.getName());
        }

        @Test
        void 식별자로_검색이_되어야_한다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("id", 소녀시대.getId().toString(), pageable);

            // when
            var response = adminArtistV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .map(AdminArtistV1Response::name)
                .containsExactlyInAnyOrder(소녀시대.getName());
        }

        @Test
        void 이름이_포함된_검색이_되어야_한다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("name", "에픽", pageable);

            // when
            var response = adminArtistV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .map(AdminArtistV1Response::name)
                .containsExactly(에픽하이.getName());
        }

        @Test
        void 이름으로_검색할때_한_글자이면_동등_검색이_되어야_한다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("name", "벤", pageable);

            // when
            var response = adminArtistV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .map(AdminArtistV1Response::name)
                .containsExactly(벤.getName());
        }

        @Test
        void 검색_필터가_비어있으면_필터링이_적용되지_않는다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("", "글렌", pageable);

            // when
            var response = adminArtistV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .hasSize(5);
        }

        @Test
        void 검색어가_비어있으면_필터링이_적용되지_않는다() {
            // given
            Pageable pageable = Pageable.ofSize(10);
            SearchCondition searchCondition = new SearchCondition("id", "", pageable);

            // when
            var response = adminArtistV1QueryService.findAll(searchCondition);

            assertThat(response.getContent())
                .hasSize(5);
        }

        @Test
        void 페이지네이션이_적용_되어야_한다() {
            // given
            Pageable pageable = PageRequest.of(0, 2);
            SearchCondition searchCondition = new SearchCondition("", "", pageable);

            // when
            var response = adminArtistV1QueryService.findAll(searchCondition);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.getSize()).isEqualTo(2);
                softly.assertThat(response.getTotalPages()).isEqualTo(3);
                softly.assertThat(response.getTotalElements()).isEqualTo(5);
            });
        }
    }
}
