package com.festago.school.dto.v1;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record SchoolDetailV1Response(
    Long id,
    String name,
    String logoUrl,
    String backgroundImageUrl,
    List<SchoolSocialMediaV1Response> socialMedias
) {

    @QueryProjection
    public SchoolDetailV1Response {
    }
}
