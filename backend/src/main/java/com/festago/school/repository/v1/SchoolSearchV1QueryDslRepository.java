package com.festago.school.repository.v1;

import static com.festago.school.domain.QSchool.school;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.QSchoolSearchV1Response;
import com.festago.school.dto.v1.SchoolSearchV1Response;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolSearchV1QueryDslRepository extends QueryDslRepositorySupport {

    private static final long MAX_FETCH_SIZE = 50L;

    public SchoolSearchV1QueryDslRepository() {
        super(School.class);
    }

    public List<SchoolSearchV1Response> searchSchools(String keyword) {
        return select(
            new QSchoolSearchV1Response(
                school.id,
                school.name,
                school.logoUrl
            ))
            .from(school)
            .where(school.name.contains(keyword))
            .orderBy(school.name.asc())
            .limit(MAX_FETCH_SIZE)
            .fetch();
    }
}
