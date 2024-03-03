package com.festago.school.repository.v1;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.SchoolSearchV1Response;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolSearchV1QueryDslRepository extends QueryDslRepositorySupport {

    public SchoolSearchV1QueryDslRepository() {
        super(School.class);
    }

    public List<SchoolSearchV1Response> searchSchools(String keyword) {
        return Collections.emptyList();
    }
}
