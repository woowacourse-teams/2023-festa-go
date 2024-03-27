package com.festago.festival.application.command;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import com.festago.festival.domain.Festival;
import com.festago.festival.dto.command.FestivalUpdateCommand;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.TimeInstantProvider;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import java.time.Clock;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalUpdateServiceTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalUpdateService festivalUpdateService;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    Clock clock;

    @Nested
    class updateFestival {

        Long festivalId;
        LocalDate now = LocalDate.parse("2023-01-31");

        @BeforeEach
        void setUp() {
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
            School school = schoolRepository.save(SchoolFixture.builder().build());
            Festival festival = festivalRepository.save(FestivalFixture.builder().school(school).build());
            festivalId = festival.getId();
        }

        @Test
        void 시작일이_현재_시간보다_과거여도_수정할_수_있다() {
            // given
            String newFestivalName = "변경된 축제";
            LocalDate newStartDate = now.minusDays(1);
            LocalDate newEndDate = now.plusDays(1);
            String newThumbnail = "https://image.com/new-image.png";
            var command = new FestivalUpdateCommand(
                newFestivalName,
                newStartDate,
                newEndDate,
                newThumbnail
            );

            // when
            festivalUpdateService.updateFestival(festivalId, command);

            // then
            Festival updatedFestival = festivalRepository.getOrThrow(festivalId);
            assertSoftly(softly -> {
                softly.assertThat(updatedFestival.getName()).isEqualTo(newFestivalName);
                softly.assertThat(updatedFestival.getStartDate()).isEqualTo(newStartDate);
                softly.assertThat(updatedFestival.getEndDate()).isEqualTo(newEndDate);
                softly.assertThat(updatedFestival.getThumbnail()).isEqualTo(newThumbnail);
            });
        }

        @Test
        void 축제를_수정할_수_있다() {
            // given
            String newFestivalName = "변경된 축제";
            LocalDate newStartDate = now.plusDays(1);
            LocalDate newEndDate = now.plusDays(1);
            String newThumbnail = "https://image.com/new-image.png";
            var command = new FestivalUpdateCommand(
                newFestivalName,
                newStartDate,
                newEndDate,
                newThumbnail
            );

            // when
            festivalUpdateService.updateFestival(festivalId, command);

            // then
            Festival updatedFestival = festivalRepository.getOrThrow(festivalId);
            assertSoftly(softly -> {
                softly.assertThat(updatedFestival.getName()).isEqualTo(newFestivalName);
                softly.assertThat(updatedFestival.getStartDate()).isEqualTo(newStartDate);
                softly.assertThat(updatedFestival.getEndDate()).isEqualTo(newEndDate);
                softly.assertThat(updatedFestival.getThumbnail()).isEqualTo(newThumbnail);
            });
        }
    }
}
