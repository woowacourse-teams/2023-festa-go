package com.festago.ticket.domain.validator.stage;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.festago.stage.domain.Stage;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.support.fixture.StageTicketFixture;
import com.festago.ticket.repository.MemoryStageTicketRepository;
import com.festago.ticket.repository.StageTicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ExistsTicketStageDeleteValidatorTest {

    ExistsTicketStageDeleteValidator existsTicketStageDeleteValidator;

    StageTicketRepository stageTicketRepository;

    @BeforeEach
    void setUp() {
        stageTicketRepository = new MemoryStageTicketRepository();
        existsTicketStageDeleteValidator = new ExistsTicketStageDeleteValidator(stageTicketRepository);
    }

    @Nested
    class validate {

        @Test
        void 공연에_티켓이_등록_되어있으면_예외() {
            // given
            School school = SchoolFixture.builder().id(1L).build();
            Festival festival = FestivalFixture.builder().school(school).build();
            Stage stage = StageFixture.builder().festival(festival).build();
            stageTicketRepository.save(StageTicketFixture.builder().schoolId(school.getId()).stage(stage).build());

            // when & then
            assertThatThrownBy(() -> existsTicketStageDeleteValidator.validate(stage))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.STAGE_DELETE_CONSTRAINT_EXISTS_TICKET.getMessage());
        }

        @Test
        void 공연에_티켓이_없으면_예외가_발생하지_않는다() {
            // given
            Stage stage = StageFixture.builder().build();

            // when & then
            assertDoesNotThrow(() -> existsTicketStageDeleteValidator.validate(stage));
        }
    }
}
