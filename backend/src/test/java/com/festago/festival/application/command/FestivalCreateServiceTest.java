package com.festago.festival.application.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
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
class FestivalCreateServiceTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalCreateService festivalCreateService;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

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
            assertThatThrownBy(() -> festivalCreateService.createFestival(command))
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
            Long festivalId = festivalCreateService.createFestival(command);

            // then
            assertSoftly(softly -> {
                softly.assertThat(festivalRepository.findById(festivalId)).isPresent();
                softly.assertThat(festivalInfoRepository.findByFestivalId(festivalId)).isPresent();
            });
        }
    }
}
