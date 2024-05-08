package com.festago.admin.dto.socialmedia;

import com.festago.socialmedia.dto.command.SocialMediaUpdateCommand;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SocialMediaUpdateV1Request(
    @NotBlank
    String name,
    @Nullable
    String logoUrl,
    @NotBlank
    String url
) {

    public SocialMediaUpdateCommand toCommand() {
        return SocialMediaUpdateCommand.builder()
            .name(name)
            .logoUrl(logoUrl)
            .url(url)
            .build();
    }
}
