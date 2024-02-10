package com.festago.festival.dto;

import com.festago.socialmedia.domain.SocialMediaType;

public record SocialMediaV1Response(
    SocialMediaType type,
    String name,
    String logoUrl,
    String url
) {

}
