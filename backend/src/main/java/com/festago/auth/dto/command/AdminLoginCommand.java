package com.festago.auth.dto.command;

import lombok.Builder;

@Builder
public record AdminLoginCommand(
    String username,
    String password
) {

}
