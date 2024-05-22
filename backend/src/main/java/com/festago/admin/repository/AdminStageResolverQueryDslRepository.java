package com.festago.admin.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.stage.domain.QStage.stage;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.stage.domain.Stage;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AdminStageResolverQueryDslRepository extends QueryDslRepositorySupport {

    public AdminStageResolverQueryDslRepository() {
        super(Stage.class);
    }

    public List<Stage> findStageByFestivalId(Long festivalId) {
        return selectFrom(stage)
            .innerJoin(stage.festival)
            .leftJoin(stage.artists).fetchJoin()
            .where(festival.id.eq(festivalId))
            .fetch();
    }

    public List<Stage> findStageByFestivalIdIn(List<Long> festivalIds) {
        return selectFrom(stage)
            .innerJoin(stage.festival)
            .leftJoin(stage.artists).fetchJoin()
            .where(festival.id.in(festivalIds))
            .fetch();
    }
}
