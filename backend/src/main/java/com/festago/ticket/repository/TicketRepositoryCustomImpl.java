package com.festago.ticket.repository;

import static com.festago.stage.domain.QStage.stage;
import static com.festago.ticket.domain.QTicket.ticket;
import static com.festago.ticket.domain.QTicketEntryTime.ticketEntryTime;

import com.festago.ticket.domain.Ticket;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Deprecated(forRemoval = true)
@RequiredArgsConstructor
public class TicketRepositoryCustomImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Ticket> findAllByStageIdWithFetch(Long stageId) {
        return queryFactory.selectFrom(ticket)
                .innerJoin(ticket.ticketAmount).fetchJoin()
                .where(ticket.stage.id.eq(stageId))
                .fetch();
    }

    @Override
    public Optional<Ticket> findByIdWithFetch(Long ticketId) {
        return Optional.ofNullable(queryFactory.selectFrom(ticket)
                .innerJoin(ticket.stage, stage).fetchJoin()
                .innerJoin(ticket.ticketEntryTimes, ticketEntryTime).fetchJoin()
                .where(ticket.id.eq(ticketId))
                .fetchOne());
    }
}
