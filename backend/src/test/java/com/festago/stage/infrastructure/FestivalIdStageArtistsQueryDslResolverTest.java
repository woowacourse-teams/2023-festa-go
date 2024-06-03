package com.festago.stage.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageArtistFixture;
import com.festago.support.fixture.StageFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalIdStageArtistsQueryDslResolverTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    StageArtistRepository stageArtistRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    FestivalIdStageArtistsQueryDslResolver festivalIdStageArtistsResolver;

    Long festivalId;

    Stage 테코대학교_두번째_공연;

    Artist 뉴진스;
    Artist 에픽하이;
    Artist 아이유;

    @BeforeEach
    void setUp() {
        School 테코대학교 = schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());
        뉴진스 = artistRepository.save(ArtistFixture.builder().name("뉴진스").build());
        에픽하이 = artistRepository.save(ArtistFixture.builder().name("에픽하이").build());
        아이유 = artistRepository.save(ArtistFixture.builder().name("아이유").build());
        Festival 테코대학교_축제 = festivalRepository.save(FestivalFixture.builder().school(테코대학교).build());
        Stage 테코대학교_첫번째_공연 = stageRepository.save(StageFixture.builder().festival(테코대학교_축제).build());
        테코대학교_두번째_공연 = stageRepository.save(StageFixture.builder().festival(테코대학교_축제).build());
        stageArtistRepository.save(StageArtistFixture.builder(테코대학교_첫번째_공연.getId(), 뉴진스.getId()).build());
        stageArtistRepository.save(StageArtistFixture.builder(테코대학교_두번째_공연.getId(), 에픽하이.getId()).build());
        stageArtistRepository.save(StageArtistFixture.builder(테코대학교_두번째_공연.getId(), 아이유.getId()).build());
        festivalId = 테코대학교_축제.getId();
    }

    @Test
    void 축제의_식별자로_축제의_공연에_참여하는_아티스트를_모두_조회한다() {
        // when
        List<Artist> expect = festivalIdStageArtistsResolver.resolve(festivalId);

        // then
        assertThat(expect)
            .map(Artist::getName)
            .containsOnly(뉴진스.getName(), 에픽하이.getName(), 아이유.getName());
    }
}
