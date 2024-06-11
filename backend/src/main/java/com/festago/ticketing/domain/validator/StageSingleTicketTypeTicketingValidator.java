package com.festago.ticketing.domain.validator;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.ticket.domain.NewTicket;
import com.festago.ticket.domain.StageTicket;
import com.festago.ticketing.domain.Booker;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 공연의 티켓에 대해 하나의 유형에 대해서만 예매가 가능하도록 검증하는 클래스 <br/> ex) 사용자가 하나의 공연에 대해 학생 전용, 외부인 전용을 모두 예매하는 상황을 방지 <br/>
 */
@Component
@RequiredArgsConstructor
public class StageSingleTicketTypeTicketingValidator implements TicketingValidator {

    private final StageReservedTicketIdResolver stageReservedTicketIdResolver;

    @Override
    public void validate(NewTicket ticket, Booker booker) {
        if (!(ticket instanceof StageTicket stageTicket)) {
            return;
        }
        Long memberId = booker.getMemberId();
        Long stageId = stageTicket.getStage().getId();
        Long ticketId = stageReservedTicketIdResolver.resolve(memberId, stageId);
        if (ticketId == null || Objects.equals(ticketId, ticket.getId())) {
            return;
        }
        throw new BadRequestException(ErrorCode.ONLY_STAGE_TICKETING_SINGLE_TYPE);
    }
}
