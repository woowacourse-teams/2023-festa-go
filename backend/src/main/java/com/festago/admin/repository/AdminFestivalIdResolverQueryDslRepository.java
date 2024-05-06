package com.festago.admin.repository;

import static com.festago.festival.domain.QFestival.festival;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.domain.Festival;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AdminFestivalIdResolverQueryDslRepository extends QueryDslRepositorySupport {

    public AdminFestivalIdResolverQueryDslRepository() {
        super(Festival.class);
    }

    public List<Long> findFestivalIdsByStartDatePeriod(LocalDate to, LocalDate end) {
        return select(festival.id)
            .from(festival)
            .where(festival.festivalDuration.startDate.goe(to)
                .and(festival.festivalDuration.startDate.loe(end)))
            .fetch();
    }
}
