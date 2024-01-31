package com.festago.festival.application.integration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.application.FestivalCommandService;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.festival.dto.command.FestivalUpdateCommand;
import com.festago.festival.repository.FestivalQueryInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.FestivalFixture;
import com.festago.support.SchoolFixture;
import com.festago.support.TimeInstantProvider;
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
class FestivalCommandServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalCommandService festivalCommandService;

    @Autowired
    FestivalQueryInfoRepository festivalQueryInfoRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    Clock clock;

    @Nested
    class createFestival {

        Long schoolId;
        LocalDate now = LocalDate.parse("2023-01-31");

        @BeforeEach
        void setUp() {
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
            School school = schoolRepository.save(SchoolFixture.school().build());
            schoolId = school.getId();
        }

        @Test
        void 축제의_시작일이_현재_시간보다_과거이면_예외가_발생한다() {
            // given
            String festivalName = "테코대학교 축제";
            LocalDate startDate = now.minusDays(1);
            LocalDate endDate = now.plusDays(3);
            String thumbnail = "https://image.com/image.png";
            var command = new FestivalCreateCommand(
                festivalName,
                startDate,
                endDate,
                thumbnail,
                schoolId
            );

            // when & then
            assertThatThrownBy(() -> festivalCommandService.createFestival(command))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_FESTIVAL_START_DATE.getMessage());
        }

        @Test
        void 축제를_생성하면_축제가_저장되고_FestivalQueryInfo도_저장된다() {
            // given
            String festivalName = "테코대학교 축제";
            LocalDate startDate = now.plusDays(1);
            LocalDate endDate = now.plusDays(3);
            String thumbnail = "https://image.com/image.png";
            var command = new FestivalCreateCommand(
                festivalName,
                startDate,
                endDate,
                thumbnail,
                schoolId
            );

            // when
            Long festivalId = festivalCommandService.createFestival(command);

            // then
            assertSoftly(softly -> {
                softly.assertThat(festivalRepository.findById(festivalId)).isPresent();
                softly.assertThat(festivalQueryInfoRepository.findByFestivalId(festivalId)).isPresent();
            });
        }
    }

    @Nested
    class updateFestival {

        Long festivalId;
        LocalDate now = LocalDate.parse("2023-01-31");

        @BeforeEach
        void setUp() {
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
            School school = schoolRepository.save(SchoolFixture.school().build());
            Festival festival = festivalRepository.save(FestivalFixture.festival().school(school).build());
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
            festivalCommandService.updateFestival(festivalId, command);

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
            festivalCommandService.updateFestival(festivalId, command);

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
