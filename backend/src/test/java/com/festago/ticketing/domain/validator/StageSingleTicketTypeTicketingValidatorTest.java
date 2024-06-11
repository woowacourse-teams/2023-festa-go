package com.festago.ticketing.domain.validator;

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
import com.festago.ticket.domain.FakeTicket;
import com.festago.ticket.domain.StageTicket;
import com.festago.ticketing.domain.Booker;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageSingleTicketTypeTicketingValidatorTest {

    StageSingleTicketTypeTicketingValidator validator;

    Long ticketId = 1L;
    Long otherTicketId = 2L;
    Long schoolId = 1L;
    Long memberId = 1L;

    @Test
    void 티켓의_식별자가_다르면_예외() {
        // given
        StageTicket ticket = createStageTicket(ticketId, schoolId);
        Booker booker = new Booker(memberId, schoolId);
        validator = new StageSingleTicketTypeTicketingValidator((memberId, stageId) -> otherTicketId);

        // when & then
        assertThatThrownBy(() -> validator.validate(ticket, booker))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ErrorCode.ONLY_STAGE_TICKETING_SINGLE_TYPE.getMessage());
    }

    @Test
    void 티켓의_식별자와_같으면_예외가_발생하지_않는다() {
        // given
        StageTicket ticket = createStageTicket(ticketId, schoolId);
        Booker booker = new Booker(memberId, schoolId);
        validator = new StageSingleTicketTypeTicketingValidator((memberId, stageId) -> ticketId);

        // when & then
        assertDoesNotThrow(() -> validator.validate(ticket, booker));
    }

    @Test
    void 티켓의_타입이_StageTicket이_아니라면_티켓의_식별자가_달라도_예외가_발생하지_않는다() {
        // given
        FakeTicket ticket = new FakeTicket(ticketId, 100);
        Booker booker = new Booker(memberId, schoolId);
        validator = new StageSingleTicketTypeTicketingValidator((memberId, stageId) -> otherTicketId);

        // when & then
        assertDoesNotThrow(() -> validator.validate(ticket, booker));
    }

    private StageTicket createStageTicket(Long ticketId, Long schoolId) {
        School school = SchoolFixture.builder().id(schoolId).build();
        Festival festival = FestivalFixture.builder().school(school).build();
        Stage stage = StageFixture.builder().festival(festival).build();
        return StageTicketFixture.builder().id(ticketId).stage(stage).schoolId(schoolId).build();
    }
}
