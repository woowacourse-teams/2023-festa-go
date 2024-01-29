package com.festago.festival.dto;

import java.util.List;

public record PopularFestivalsV1Response(
    String title,
    List<FestivalV1Response> content) {

}
