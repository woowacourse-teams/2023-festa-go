package com.festago.artist.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.artist.application.ArtistDetailV1QueryService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistDetailV1Response;
import com.festago.artist.dto.ArtistFestivalDetailV1Response;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalInfoSerializer;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.repository.SocialMediaRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArtistDetailV1QueryServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    StageArtistRepository stageArtistRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    FestivalInfoSerializer festivalInfoSerializer;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    @Autowired
    ArtistDetailV1QueryService artistDetailV1QueryService;

    @Nested
    class 아티스트_정보_는 {

        @Test
        void 조회할_수_있다() {
            // given
            Artist pooh = artistRepository.save(new Artist("pooh", "image.jpg"));
            Long id = pooh.getId();
            makeArtistSocialMedia(id, OwnerType.ARTIST, SocialMediaType.INSTAGRAM);
            makeArtistSocialMedia(id, OwnerType.ARTIST, SocialMediaType.YOUTUBE);

            // when
            ArtistDetailV1Response acutal = artistDetailV1QueryService.findArtistDetail(id);

            // then
            assertThat(acutal.socialMedias()).hasSize(2);
        }

        @Test
        void 소셜_매체가_없더라도_반환한다() {
            // given
            Artist pooh = artistRepository.save(new Artist("pooh", "image.jpg"));
            Long id = pooh.getId();

            // when
            ArtistDetailV1Response acutal = artistDetailV1QueryService.findArtistDetail(id);

            // then
            assertThat(acutal.socialMedias()).isEmpty();
        }

        @Test
        void 소셜_매체의_주인_아이디가_같더라도_주인의_타입에_따라_구분하여_반환한다() {
            // given
            Artist pooh = artistRepository.save(new Artist("pooh", "image.jpg"));
            Long id = pooh.getId();
            makeArtistSocialMedia(id, OwnerType.ARTIST, SocialMediaType.INSTAGRAM);
            makeArtistSocialMedia(id, OwnerType.SCHOOL, SocialMediaType.INSTAGRAM);

            // when
            ArtistDetailV1Response acutal = artistDetailV1QueryService.findArtistDetail(id);

            // then
            assertThat(acutal.socialMedias()).hasSize(1);
        }

        @Test
        void 존재하지_않는_아티스트를_검색하면_에외() {
            // given & when & then
            assertThatThrownBy(() -> artistDetailV1QueryService.findArtistDetail(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ARTIST_NOT_FOUND.getMessage());

        }

        SocialMedia makeArtistSocialMedia(Long id, OwnerType ownerType, SocialMediaType socialMediaType) {
            return socialMediaRepository.save(
                new SocialMedia(id, ownerType, socialMediaType, "총학생회", "profileImageUrl", "url"));
        }
    }

    @Nested
    class 아티스트_축제_는 {

        LocalDate nowDate;
        LocalDateTime nowDateTime;
        Artist 푸우;
        Artist 오리;
        Artist 글렌;

        @BeforeEach
        void setting() {
            nowDate = LocalDate.now();
            nowDateTime = LocalDateTime.now();

            School 부산_학교 = schoolRepository.save(new School("domain1", "부산 학교", SchoolRegion.부산));
            School 서울_학교 = schoolRepository.save(new School("domain2", "서울 학교", SchoolRegion.서울));
            School 대구_학교 = schoolRepository.save(new School("domain3", "대구 학교", SchoolRegion.대구));

            Festival 부산_축제 = festivalRepository.save(
                new Festival("부산 축제", nowDate.minusDays(5), nowDate.minusDays(1), 부산_학교));
            Festival 서울_축제 = festivalRepository.save(
                new Festival("서울 축제", nowDate.minusDays(1), nowDate.plusDays(3), 서울_학교));
            Festival 대구_축제 = festivalRepository.save(
                new Festival("대구 축제", nowDate.plusDays(1), nowDate.plusDays(5), 대구_학교));

            Stage 부산_공연 = stageRepository.save(new Stage(nowDateTime.minusDays(5L), nowDateTime.minusDays(6L), 부산_축제));
            Stage 서울_공연 = stageRepository.save(new Stage(nowDateTime.minusDays(1L), nowDateTime.minusDays(2L), 서울_축제));
            Stage 대구_공연 = stageRepository.save(new Stage(nowDateTime.plusDays(1L), nowDateTime, 대구_축제));

            푸우 = artistRepository.save(new Artist("푸우", "푸우.jpg"));
            오리 = artistRepository.save(new Artist("오리", "오리.jpg"));
            글렌 = artistRepository.save(new Artist("글렌", "글렌.jpg"));

            stageArtistRepository.save(new StageArtist(부산_공연.getId(), 푸우.getId()));
            stageArtistRepository.save(new StageArtist(부산_공연.getId(), 오리.getId()));
            festivalInfoRepository.save(FestivalQueryInfo.of(부산_축제, List.of(푸우, 오리), festivalInfoSerializer));

            stageArtistRepository.save(new StageArtist(서울_공연.getId(), 푸우.getId()));
            stageArtistRepository.save(new StageArtist(서울_공연.getId(), 글렌.getId()));
            festivalInfoRepository.save(FestivalQueryInfo.of(서울_축제, List.of(푸우, 글렌), festivalInfoSerializer));

            stageArtistRepository.save(new StageArtist(대구_공연.getId(), 오리.getId()));
            stageArtistRepository.save(new StageArtist(대구_공연.getId(), 글렌.getId()));
            festivalInfoRepository.save(FestivalQueryInfo.of(대구_축제, List.of(오리, 글렌), festivalInfoSerializer));
        }

        @Test
        void 진행중인_축제_조회가_가능하다() {
            // given & when
            Slice<ArtistFestivalDetailV1Response> actual = artistDetailV1QueryService.findArtistFestivals(
                오리.getId(),
                null,
                null,
                false,
                PageRequest.ofSize(10)
            );

            // then
            assertThat(actual.getContent()).hasSize(1);
        }

        @Test
        void 종료된_축제_조회가_가능하다() {
            // given & when
            Slice<ArtistFestivalDetailV1Response> actual = artistDetailV1QueryService.findArtistFestivals(
                오리.getId(),
                null,
                null,
                true,
                PageRequest.ofSize(10)
            );

            // then
            assertThat(actual.getContent()).hasSize(1);
        }

        @Test
        void 커서_기반_검색이_가능하다() {
            // given
            PageRequest pageable = PageRequest.ofSize(1);

            Slice<ArtistFestivalDetailV1Response> firstResponse = artistDetailV1QueryService.findArtistFestivals(
                글렌.getId(),
                null,
                null,
                false,
                pageable
            );

            ArtistFestivalDetailV1Response firstFestivalResponse = firstResponse.getContent().get(0);

            // when
            Slice<ArtistFestivalDetailV1Response> secondResponse = artistDetailV1QueryService.findArtistFestivals(
                글렌.getId(),
                firstFestivalResponse.id(),
                firstFestivalResponse.startDate(),
                false,
                pageable
            );

            // then
            assertSoftly(softly -> {
                softly.assertThat(firstResponse.isLast()).isFalse();
                softly.assertThat(secondResponse.isLast()).isTrue();
            });
        }
    }
}
