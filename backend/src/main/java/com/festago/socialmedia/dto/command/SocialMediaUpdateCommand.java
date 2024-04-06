package com.festago.socialmedia.dto.command;

public record SocialMediaUpdateCommand(
    String name,
    String url,
    String logoUrl
) {

}
