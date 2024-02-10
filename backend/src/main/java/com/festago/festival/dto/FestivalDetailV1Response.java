package com.festago.festival.dto;

import java.time.LocalDate;
import java.util.List;

public record FestivalDetailV1Response(
    Long id,
    String name,
    SchoolV1Response school,
    LocalDate startDate,
    LocalDate endDate,
    String imageUrl,
    List<SocialMediaV1Response> socialMedias,
    List<StageV1Response> stages
) {

}
