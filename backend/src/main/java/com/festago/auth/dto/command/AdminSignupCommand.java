package com.festago.auth.dto.command;

import lombok.Builder;

@Builder
public record AdminSignupCommand(
    String username,
    String password
) {

}
