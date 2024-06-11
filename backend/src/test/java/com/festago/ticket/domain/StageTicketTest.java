package com.festago.ticket.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import com.festago.stage.domain.Stage;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.SchoolFixture;
import com.festago.support.fixture.StageFixture;
import com.festago.support.fixture.StageTicketFixture;
import com.festago.ticketing.domain.Booker;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StageTicketTest {

    LocalDateTime _6월_12일_17시_0분 = LocalDateTime.parse("2077-06-12T17:00:00");
    LocalDateTime _6월_12일_18시_0분 = LocalDateTime.parse("2077-06-12T18:00:00");
    LocalDateTime _6월_15일_17시_0분 = LocalDateTime.parse("2077-06-15T17:00:00");
    LocalDateTime _6월_15일_18시_0분 = LocalDateTime.parse("2077-06-15T18:00:00");
    Long schoolId = 1L;

    @Nested
    class 생성자 {

        @Test
        void 생성하려는_티켓의_학교_식별자와_공연의_식별자가_다르면_예외() {
            // given
            School school = SchoolFixture.builder().id(schoolId).build();
            Festival festival = FestivalFixture.builder()
                .school(school)
                .startDate(_6월_15일_18시_0분.toLocalDate())
                .endDate(_6월_15일_18시_0분.toLocalDate())
                .build();
            Stage stage = StageFixture.builder()
                .festival(festival)
                .startTime(_6월_15일_18시_0분)
                .ticketOpenTime(_6월_15일_17시_0분)
                .build();

            assertThatThrownBy(() -> new StageTicket(4885L, TicketExclusive.NONE, stage))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.NOT_ENOUGH_PERMISSION.getMessage());
        }
    }

    @Nested
    class addTicketEntryTime {

        @Test
        void 파라미터의_학교_식별자와_티켓의_학교_식별자가_다르면_예외() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);

            // when & then
            LocalDateTime now = _6월_12일_17시_0분;
            LocalDateTime entryTime = _6월_15일_18시_0분.minusHours(1);
            assertThatThrownBy(() -> stageTicket.addTicketEntryTime(4885L, now, entryTime, 100))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.NOT_ENOUGH_PERMISSION.getMessage());
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1, 2})
        void 티켓_오픈_시간_이후에_티켓_입장_시간을_추가하면_예외(long second) {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);

            // when & then
            LocalDateTime now = _6월_12일_18시_0분.plusSeconds(second);
            LocalDateTime entryTime = _6월_15일_18시_0분.minusHours(1);
            Long schoolId = stageTicket.getSchoolId();
            assertThatThrownBy(() -> stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_TICKET_CREATE_TIME.getMessage());
        }

        @ParameterizedTest
        @ValueSource(longs = {1, 2})
        void 입장_시간이_티켓_오픈_시간_이전이면_예외(long second) {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_15일_17시_0분, TicketExclusive.NONE);

            // when & then
            LocalDateTime now = _6월_12일_17시_0분;
            LocalDateTime entryTime = _6월_12일_17시_0분.minusSeconds(second);

            assertThatThrownBy(() -> stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.EARLY_TICKET_ENTRY_THAN_OPEN.getMessage());
        }

        @ParameterizedTest
        @ValueSource(longs = {1, 2})
        void 입장_시간이_공연_시작_시간_이후이면_예외(long second) {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);

            // when & then
            LocalDateTime now = _6월_12일_17시_0분;
            LocalDateTime entryTime = _6월_15일_18시_0분.plusSeconds(second);
            assertThatThrownBy(() -> stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.LATE_TICKET_ENTRY_TIME.getMessage());
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1, 2})
        void 입장_시간이_공연_시작_시간보다_12시간_더_빠르면_예외(long second) {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);

            // when & then
            LocalDateTime now = _6월_12일_17시_0분;
            LocalDateTime entryTime = _6월_15일_18시_0분.minusHours(12).minusSeconds(second);
            assertThatThrownBy(() -> stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.EARLY_TICKET_ENTRY_TIME.getMessage());
        }

        @Test
        void 티켓_입장_시간을_추가하면_티켓_수량이_추가된다() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);

            // when
            LocalDateTime now = _6월_12일_17시_0분;
            LocalDateTime entryTime = _6월_15일_18시_0분.minusHours(1);
            stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100);

            // then
            assertThat(stageTicket.getAmount()).isEqualTo(100);
        }
    }

    @Nested
    class validateReserve {

        @Test
        void 티켓이_재학생_전용이고_예매자가_학생이_아닌_경우_예외() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.STUDENT);
            stageTicket.addTicketEntryTime(schoolId, _6월_12일_17시_0분, _6월_15일_17시_0분, 100);
            Booker booker = Booker.builder().memberId(schoolId).build();

            // when
            assertThatThrownBy(() -> stageTicket.validateReserve(booker, _6월_12일_18시_0분))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.RESERVE_TICKET_NOT_SCHOOL_STUDENT.getMessage());
        }

        @Test
        void 티켓이_재학생_전용이고_예매자가_해당_학교의_학생이_아닌_경우_예외() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.STUDENT);
            stageTicket.addTicketEntryTime(schoolId, _6월_12일_17시_0분, _6월_15일_17시_0분, 100);
            Booker booker = Booker.builder().memberId(schoolId).schoolId(4885L).build();

            // when
            assertThatThrownBy(() -> stageTicket.validateReserve(booker, _6월_12일_18시_0분))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.RESERVE_TICKET_NOT_SCHOOL_STUDENT.getMessage());
        }

        @Test
        void 티켓의_공연이_시작_이후이면_예외() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_17시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);
            stageTicket.addTicketEntryTime(schoolId, _6월_12일_17시_0분, _6월_15일_17시_0분, 100);
            Booker booker = Booker.builder().memberId(schoolId).schoolId(4885L).build();

            // when
            assertThatThrownBy(() -> stageTicket.validateReserve(booker, _6월_15일_18시_0분))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.TICKET_CANNOT_RESERVE_STAGE_START.getMessage());
        }

        @Test
        void 티켓_예매_시간_이전에_예매를_시도하면_예외() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_17시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);
            stageTicket.addTicketEntryTime(schoolId, _6월_12일_17시_0분, _6월_15일_17시_0분, 100);
            Booker booker = Booker.builder().memberId(schoolId).schoolId(4885L).build();

            // when
            assertThatThrownBy(() -> stageTicket.validateReserve(booker, _6월_12일_17시_0분))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.RESERVE_TICKET_BEFORE_TICKET_OPEN_TIME.getMessage());
        }

        @Test
        void 조건에_이상이_없으면_예외가_발생하지_않는다() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_17시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);
            stageTicket.addTicketEntryTime(schoolId, _6월_12일_17시_0분, _6월_15일_17시_0분, 100);
            Booker booker = Booker.builder().memberId(schoolId).schoolId(4885L).build();

            // when
            assertThatNoException().isThrownBy(() -> stageTicket.validateReserve(booker, _6월_15일_17시_0분));
        }
    }

    @Nested
    class getTicketingEndTime {

        @Test
        void 티켓팅이_종료되는_시간은_공연의_시작_시간이다() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_17시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);

            // when
            LocalDateTime ticketingEndTime = stageTicket.getTicketingEndTime();

            // then
            Stage stage = stageTicket.getStage();
            assertThat(ticketingEndTime).isEqualTo(stage.getStartTime());
        }
    }

    @Nested
    class deleteTicketEntryTime {

        @Test
        void 파라미터의_학교_식별자와_티켓의_학교_식별자가_다르면_예외() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);
            LocalDateTime now = _6월_12일_17시_0분;
            LocalDateTime entryTime = _6월_15일_18시_0분.minusHours(1);

            // when & then
            assertThatThrownBy(() -> stageTicket.deleteTicketEntryTime(4885L, now, entryTime))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.NOT_ENOUGH_PERMISSION.getMessage());
        }

        @Test
        void 티켓_오픈_시간_이후에_삭제하면_예외() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);
            LocalDateTime now = stageTicket.getStage().getTicketOpenTime().plusSeconds(1);
            LocalDateTime entryTime = _6월_15일_18시_0분.minusHours(1);

            // when & then
            assertThatThrownBy(() -> stageTicket.deleteTicketEntryTime(schoolId, now, entryTime))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.STAGE_TICKET_DELETE_CONSTRAINT_TICKET_OPEN_TIME.getMessage());
        }

        @Test
        void 삭제하려는_입장_시간이_존재하지_않으면_거짓이_반환된다() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);
            LocalDateTime now = _6월_12일_17시_0분;
            LocalDateTime entryTime = _6월_15일_18시_0분.minusHours(1);
            stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100);

            // when
            boolean actual = stageTicket.deleteTicketEntryTime(schoolId, now, entryTime.minusSeconds(1));

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 삭제하려는_입장_시간이_존재하면_참이_반환되고_수량이_반영된다() {
            // given
            StageTicket stageTicket = createStageTicket(_6월_15일_18시_0분, _6월_12일_18시_0분, TicketExclusive.NONE);
            LocalDateTime now = _6월_12일_17시_0분;
            LocalDateTime entryTime = _6월_15일_18시_0분.minusHours(1);
            stageTicket.addTicketEntryTime(schoolId, now, entryTime, 100);
            stageTicket.addTicketEntryTime(schoolId, now, entryTime.plusSeconds(1), 50);

            // when
            boolean actual = stageTicket.deleteTicketEntryTime(schoolId, now, entryTime);

            // then
            assertThat(actual).isTrue();
            assertThat(stageTicket.getAmount()).isEqualTo(50);
        }
    }

    StageTicket createStageTicket(
        LocalDateTime stageStartTime,
        LocalDateTime stageTicketOpenTime,
        TicketExclusive ticketExclusive
    ) {
        School school = SchoolFixture.builder().id(schoolId).build();
        Festival festival = FestivalFixture.builder()
            .school(school)
            .startDate(stageStartTime.toLocalDate())
            .endDate(stageStartTime.toLocalDate())
            .build();
        Stage stage = StageFixture.builder()
            .festival(festival)
            .startTime(stageStartTime)
            .ticketOpenTime(stageTicketOpenTime)
            .build();
        return StageTicketFixture.builder().id(1L).schoolId(school.getId()).stage(stage)
            .ticketExclusive(ticketExclusive).build();
    }
}
