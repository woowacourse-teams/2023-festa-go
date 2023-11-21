package com.festago.ticket.repository;

import static com.festago.ticket.domain.QTicketAmount.ticketAmount;

import com.festago.ticket.domain.TicketAmount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TicketAmountRepositoryCustomImpl implements TicketAmountRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TicketAmount> findByTicketIdForUpdate(Long ticketId) {
        return Optional.ofNullable(queryFactory.selectFrom(ticketAmount)
                .where(ticketAmount.id.eq(ticketId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
