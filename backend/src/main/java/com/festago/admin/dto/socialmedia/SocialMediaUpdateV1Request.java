package com.festago.admin.dto.socialmedia;

import com.festago.socialmedia.dto.command.SocialMediaUpdateCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SocialMediaUpdateV1Request(
    @NotBlank
    String name,
    @NotBlank
    String logoUrl,
    @NotBlank
    String url
) {

    public SocialMediaUpdateCommand toCommand() {
        return new SocialMediaUpdateCommand(
            name,
            logoUrl,
            url
        );
    }
}
