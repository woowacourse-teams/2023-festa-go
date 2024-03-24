package com.festago.festival.application.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.TimeInstantProvider;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalDeleteServiceTest extends ApplicationIntegrationTest {

    @Autowired
    FestivalDeleteService festivalDeleteService;

    @Autowired
    FestivalInfoRepository festivalInfoRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    Clock clock;

    @Nested
    class deleteFestival {

        Long festivalId;
        LocalDate now = LocalDate.parse("2023-01-31");

        @BeforeEach
        void setUp() {
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));
            School school = schoolRepository.save(SchoolFixture.builder().build());
            Festival festival = festivalRepository.save(FestivalFixture.builder()
                .startDate(now)
                .endDate(now)
                .school(school)
                .build()
            );
            festivalId = festival.getId();
        }

        @Test
        void 공연이_등록된_축제는_삭제할_수_없다() {
            // given
            Festival festival = festivalRepository.getOrThrow(festivalId);
            LocalDateTime startTime = LocalDateTime.now(clock);
            LocalDateTime ticketOpenTime = startTime.minusDays(1);
            stageRepository.save(StageFixture.builder()
                .festival(festival)
                .startTime(startTime)
                .ticketOpenTime(ticketOpenTime)
                .build()
            );

            assertThatThrownBy(() -> festivalDeleteService.deleteFestival(festivalId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.FESTIVAL_DELETE_CONSTRAINT_EXISTS_STAGE.getMessage());
        }

        @Test
        void 축제를_삭제하면_FestivalQueryInfo도_삭제된다() {
            // given
            festivalInfoRepository.save(FestivalQueryInfo.create(festivalId));

            // when
            festivalDeleteService.deleteFestival(festivalId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(festivalRepository.findById(festivalId)).isEmpty();
                softly.assertThat(festivalInfoRepository.findByFestivalId(festivalId)).isEmpty();
            });
        }
    }
}
