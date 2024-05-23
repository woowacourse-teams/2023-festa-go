package com.festago.socialmedia.dto.command;

import lombok.Builder;

@Builder
public record SocialMediaUpdateCommand(
    String name,
    String url,
    String logoUrl
) {

}
