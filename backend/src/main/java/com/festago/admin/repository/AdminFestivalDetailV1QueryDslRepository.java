package com.festago.admin.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.school.domain.QSchool.school;

import com.festago.admin.dto.AdminFestivalDetailV1Response;
import com.festago.admin.dto.QAdminFestivalDetailV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.domain.Festival;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AdminFestivalDetailV1QueryDslRepository extends QueryDslRepositorySupport {

    public AdminFestivalDetailV1QueryDslRepository() {
        super(Festival.class);
    }

    public Optional<AdminFestivalDetailV1Response> findDetail(Long festivalId) {
        return fetchOne(query -> query.select(new QAdminFestivalDetailV1Response(
                        festival.id,
                        festival.name,
                        school.id,
                        school.name,
                        festival.startDate,
                        festival.endDate,
                        festival.thumbnail,
                        festival.createdAt,
                        festival.updatedAt
                    )
                )
                .from(festival)
                .innerJoin(school).on(school.id.eq(festival.school.id))
                .where(festival.id.eq(festivalId))
        );
    }
}
