package com.festago.festival.dto;

import java.time.LocalDate;
import java.util.Set;

public record FestivalDetailV1Response(
    Long id,
    String name,
    SchoolV1Response school,
    LocalDate startDate,
    LocalDate endDate,
    String imageUrl,
    Set<SocialMediaV1Response> socialMedias,
    Set<StageV1Response> stages
) {

}
