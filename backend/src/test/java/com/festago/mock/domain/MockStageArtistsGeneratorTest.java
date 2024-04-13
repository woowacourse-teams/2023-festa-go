package com.festago.mock.domain;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.artist.domain.Artist;
import com.festago.common.exception.UnexpectedException;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.support.fixture.ArtistFixture;
import com.festago.support.fixture.StageFixture;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MockStageArtistsGeneratorTest {

    MockStageArtistsGenerator mockStageArtistsGenerator = new MockStageArtistsGenerator();

    @Test
    void 각_공연에_stagePerArtist_만큼_StageArtist를_생성한다() {
        // given
        int stagePerArtist = 3;
        int stageCount = 3;
        int artistCount = 12;
        List<Stage> stages = createStages(stageCount);
        List<Artist> artists = createArtists(artistCount);

        // when
        List<StageArtist> actual = mockStageArtistsGenerator.generate(stagePerArtist, stages, artists);

        // then
        Map<Long, Long> stageIdToArtistCount = actual.stream()
            .collect(groupingBy(StageArtist::getStageId, counting()));
        assertThat(stageIdToArtistCount.values())
            .containsOnly((long) stagePerArtist);
    }

    @Test
    void 생성된_StageArtist에는_중복된_Artist가_존재하지_않는다() {
        // given
        int stagePerArtist = 5;
        int stageCount = 2;
        int artistCount = 10;
        List<Stage> stages = createStages(stageCount);
        List<Artist> artists = createArtists(artistCount);

        // when
        long actual = mockStageArtistsGenerator.generate(stagePerArtist, stages, artists)
            .stream()
            .map(StageArtist::getArtistId)
            .distinct()
            .count();

        // then
        assertThat(actual).isEqualTo(artistCount);
    }

    private List<Stage> createStages(int stageCount) {
        return LongStream.rangeClosed(1, stageCount)
            .mapToObj(id -> StageFixture.builder().id(id).build())
            .toList();
    }

    private List<Artist> createArtists(int artistCount) {
        return LongStream.rangeClosed(1, artistCount)
            .mapToObj(id -> ArtistFixture.builder().id(id).build())
            .toList();
    }

    @Test
    void 공연의_개수가_아티스트의_개수를_초과하면_예외() {
        // given
        int stagePerArtist = 3;
        int stageCount = 11;
        int artistCount = 10;
        List<Stage> stages = createStages(stageCount);
        List<Artist> artists = createArtists(artistCount);

        // when & then
        assertThatThrownBy(() -> mockStageArtistsGenerator.generate(stagePerArtist, stages, artists))
            .isInstanceOf(UnexpectedException.class);
    }

    @Nested
    class 각_공연에_stagePerArtist_만큼_아티스트를_참여시키는_것이_불가능해도 {

        @Test
        void 각_공연마다_아티스트가_참여하는_것을_보장한다() {
            // given
            int stagePerArtist = 3;
            int stageCount = 10;
            int artistCount = 10;
            List<Stage> stages = createStages(stageCount);
            List<Artist> artists = createArtists(artistCount);

            // when
            List<StageArtist> actual = mockStageArtistsGenerator.generate(stagePerArtist, stages, artists);

            // then
            Map<Long, Long> stageIdToArtistCount = actual.stream()
                .collect(groupingBy(StageArtist::getStageId, counting()));
            assertThat(stageIdToArtistCount.values())
                .doesNotContain(0L);
        }
    }
}
