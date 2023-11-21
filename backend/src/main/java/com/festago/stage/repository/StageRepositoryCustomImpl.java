package com.festago.stage.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.stage.domain.QStage.stage;
import static com.festago.ticket.domain.QTicket.ticket;

import com.festago.stage.domain.Stage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StageRepositoryCustomImpl implements StageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Stage> findAllDetailByFestivalId(Long festivalId) {
        return queryFactory.selectFrom(stage)
                .leftJoin(stage.tickets, ticket).fetchJoin()
                .leftJoin(ticket.ticketAmount).fetchJoin()
                .where(stage.festival.id.eq(festivalId))
                .fetch();
    }

    @Override
    public Optional<Stage> findByIdWithFetch(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(stage)
                .leftJoin(stage.festival, festival).fetchJoin()
                .leftJoin(festival.school).fetchJoin()
                .where(stage.id.eq(id))
                .fetchOne());
    }
}
