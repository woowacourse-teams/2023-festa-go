package com.festago.admin.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.stage.domain.QStage.stage;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.stage.domain.Stage;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AdminStageIdResolverQueryDslRepository extends QueryDslRepositorySupport {

    public AdminStageIdResolverQueryDslRepository() {
        super(Stage.class);
    }

    public List<Long> findStageIdsByFestivalId(Long festivalId) {
        return select(stage.id)
            .from(stage)
            .innerJoin(festival).on(festival.id.eq(stage.festival.id))
            .where(festival.id.eq(festivalId))
            .fetch();
    }

    public List<Long> findStageIdsByFestivalIdIn(List<Long> festivalIds) {
        return select(stage.id)
            .from(stage)
            .innerJoin(festival).on(festival.id.eq(stage.festival.id))
            .where(festival.id.in(festivalIds))
            .fetch();
    }
}
