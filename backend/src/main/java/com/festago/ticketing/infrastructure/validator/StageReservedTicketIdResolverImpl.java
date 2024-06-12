package com.festago.ticketing.infrastructure.validator;

import static com.festago.ticket.domain.QStageTicket.stageTicket;
import static com.festago.ticketing.domain.QReserveTicket.reserveTicket;

import com.festago.common.querydsl.QueryDslHelper;
import com.festago.ticketing.domain.validator.StageReservedTicketIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StageReservedTicketIdResolverImpl implements StageReservedTicketIdResolver {

    private final QueryDslHelper queryDslHelper;

    @Override
    public Long resolve(Long memberId, Long stageId) {
        return queryDslHelper.select(stageTicket.id)
            .from(stageTicket)
            .join(reserveTicket).on(reserveTicket.ticketId.eq(stageTicket.id))
            .where(reserveTicket.memberId.eq(memberId).and(stageTicket.stage.id.eq(stageId)))
            .fetchFirst();
    }
}
