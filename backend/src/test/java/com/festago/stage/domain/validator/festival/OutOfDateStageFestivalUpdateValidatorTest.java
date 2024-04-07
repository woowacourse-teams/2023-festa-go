package com.festago.stage.domain.validator.festival;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.domain.Festival;
import com.festago.stage.repository.MemoryStageRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.StageFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OutOfDateStageFestivalUpdateValidatorTest {

    LocalDate festivalStartDate = LocalDate.parse("2077-02-19");
    LocalDate festivalEndDate = LocalDate.parse("2077-02-21");
    StageRepository stageRepository;
    OutOfDateStageFestivalUpdateValidator validator;
    Festival 축제;

    @BeforeEach
    void setUp() {
        stageRepository = new MemoryStageRepository();
        validator = new OutOfDateStageFestivalUpdateValidator(stageRepository);

        축제 = FestivalFixture.builder()
            .startDate(festivalStartDate)
            .endDate(festivalEndDate)
            .build();
    }

    @Nested
    class 축제에_등록된_공연이_있을때 {

        @BeforeEach
        void setUp() {
            LocalDateTime ticketOpenTime = festivalStartDate.atStartOfDay().minusWeeks(1);
            // 19, 20, 21 일자의 공연 생성
            for (int i = 0; i <= 2; i++) {
                stageRepository.save(
                    StageFixture.builder()
                        .festival(축제)
                        .ticketOpenTime(ticketOpenTime)
                        .startTime(festivalStartDate.plusDays(i).atTime(18, 0))
                        .build()
                );
            }
        }

        @Test
        void 축제의_일자를_확장하면_예외가_발생하지_않는다() {
            // given
            LocalDate startDate = festivalStartDate.minusDays(1);
            LocalDate endDate = festivalEndDate.plusDays(1);
            축제.changeDate(startDate, endDate);

            // when & then
            assertDoesNotThrow(() -> validator.validate(축제));
        }

        @Test
        void 축제의_시작일자를_축소하면_예외가_발생한다() {
            // given
            LocalDate startDate = festivalStartDate.plusDays(1);
            LocalDate endDate = festivalEndDate;
            축제.changeDate(startDate, endDate);

            // when & then
            assertThatThrownBy(() -> validator.validate(축제))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.FESTIVAL_UPDATE_OUT_OF_DATE_STAGE_START_TIME.getMessage());
        }

        @Test
        void 축제의_종료일자를_축소하면_예외가_발생한다() {
            // given
            LocalDate startDate = festivalStartDate;
            LocalDate endDate = festivalEndDate.minusDays(1);
            축제.changeDate(startDate, endDate);

            // when & then
            assertThatThrownBy(() -> validator.validate(축제))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.FESTIVAL_UPDATE_OUT_OF_DATE_STAGE_START_TIME.getMessage());
        }
    }

    @Nested
    class 축제에_등록된_공연이_없을때 {

        @Test
        void 축제의_일자를_확장하면_예외가_발생하지_않는다() {
            // given
            LocalDate startDate = festivalStartDate.minusDays(1);
            LocalDate endDate = festivalEndDate.plusDays(1);
            축제.changeDate(startDate, endDate);

            // when & then
            assertDoesNotThrow(() -> validator.validate(축제));
        }

        @Test
        void 축제의_시작일자를_축소하면_예외가_발생하지_않는다() {
            // given
            LocalDate startDate = festivalStartDate.plusDays(1);
            LocalDate endDate = festivalEndDate;
            축제.changeDate(startDate, endDate);

            // when & then
            assertDoesNotThrow(() -> validator.validate(축제));
        }

        @Test
        void 축제의_종료일자를_축소하면_예외가_발생하지_않는다() {
            // given
            LocalDate startDate = festivalStartDate;
            LocalDate endDate = festivalEndDate.minusDays(1);
            축제.changeDate(startDate, endDate);

            // when & then
            assertDoesNotThrow(() -> validator.validate(축제));
        }
    }
}
