package com.festago.school.dto.v1;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record SchoolDetailV1Response(
    Long id,
    String schoolName,
    String logoUrl,
    String backgroundUrl,
    List<SchoolSocialMediaV1Response> socialMedias
) {

    @QueryProjection
    public SchoolDetailV1Response {
    }
}
