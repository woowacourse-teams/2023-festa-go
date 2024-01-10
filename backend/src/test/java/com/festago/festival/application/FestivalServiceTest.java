package com.festago.festival.application;

import static com.festago.common.exception.ErrorCode.INVALID_FESTIVAL_START_DATE;
import static com.festago.common.exception.ErrorCode.SCHOOL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.FestivalResponse;
import com.festago.festival.repository.MemoryFestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.MemorySchoolRepository;
import com.festago.stage.application.FestivalStageServiceImpl;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.support.SchoolFixture;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalServiceTest {

    MemoryFestivalRepository festivalRepository = new MemoryFestivalRepository();

    MemoryStageRepository stageRepository = new MemoryStageRepository();

    MemorySchoolRepository schoolRepository = new MemorySchoolRepository();

    Clock clock = spy(Clock.systemDefaultZone());

    FestivalService festivalService = new FestivalService(
        festivalRepository,
        new FestivalStageServiceImpl(stageRepository, festivalRepository),
        schoolRepository,
        clock
    );

    @BeforeEach
    void setUp() {
        festivalRepository.clear();
        stageRepository.clear();
        schoolRepository.clear();
        reset(clock);
    }

    @Nested
    class 축제_생성 {

        @Test
        void 학교가_없으면_예외() {
            // given
            doReturn(Instant.parse("2023-01-10T16:00:00Z"))
                .when(clock)
                .instant();
            LocalDate today = LocalDate.now(clock);

            FestivalCreateRequest request = new FestivalCreateRequest(
                "테코대학교",
                today,
                today,
                "http://image.png",
                1L
            );

            // when & then
            assertThatThrownBy(() -> festivalService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(SCHOOL_NOT_FOUND.getMessage());
        }

        @Test
        void 축제_생성시_시작일자가_과거이면_예외() {
            // given
            doReturn(Instant.parse("2023-01-10T16:00:00Z"))
                .when(clock)
                .instant();
            LocalDate today = LocalDate.now(clock);

            School school = SchoolFixture.school().build();
            schoolRepository.save(school);

            FestivalCreateRequest request = new FestivalCreateRequest(
                "테코대학교",
                today.minusDays(1),
                today,
                "http://image.png",
                school.getId()
            );

            // when & then
            assertThatThrownBy(() -> festivalService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(INVALID_FESTIVAL_START_DATE.getMessage());
        }

        @Test
        void 성공하면_축제가_저장된다() {
            // given
            doReturn(Instant.parse("2023-01-10T16:00:00Z"))
                .when(clock)
                .instant();
            LocalDate today = LocalDate.now(clock);

            School school = SchoolFixture.school().build();
            schoolRepository.save(school);

            FestivalCreateRequest request = new FestivalCreateRequest(
                "테코대학교",
                today,
                today,
                "http://image.png",
                school.getId()
            );

            // when
            FestivalResponse actual = festivalService.create(request);

            // then
            assertThat(festivalRepository.findById(actual.id())).isPresent();
        }
    }
}
