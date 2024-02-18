package com.festago.school.repository.v1;

import static com.festago.school.domain.QSchool.school;
import static com.festago.socialmedia.domain.QSocialMedia.socialMedia;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.school.domain.School;
import com.festago.school.dto.v1.QSchoolDetailV1Response;
import com.festago.school.dto.v1.QSchoolSocialMediaV1Response;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.socialmedia.domain.OwnerType;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolV1QueryDslRepository extends QueryDslRepositorySupport {

    public SchoolV1QueryDslRepository() {
        super(School.class);
    }

    public SchoolDetailV1Response findById(Long schoolId) {
        List<SchoolDetailV1Response> response = selectFrom(school)
                .where(school.id.eq(schoolId))
                .leftJoin(socialMedia).on(socialMedia.ownerId.eq(schoolId)
                        .and(socialMedia.ownerType.eq(OwnerType.SCHOOL)))
                .transform(
                        groupBy(school.id).list(
                                    new QSchoolDetailV1Response(
                                        school.id, school.name, school.logoUrl, school.backgroundUrl,
                                        list(
                                                new QSchoolSocialMediaV1Response(
                                                        socialMedia.mediaType,
                                                        socialMedia.name,
                                                        socialMedia.logoUrl,
                                                        socialMedia.url
                                                ).skipNulls()
                                        )
                                )
                        )
                );

        if (response.isEmpty()) {
            throw new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND);
        }

        return response.get(0);
    }
}
