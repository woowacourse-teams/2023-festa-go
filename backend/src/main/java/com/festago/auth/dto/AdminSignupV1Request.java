package com.festago.auth.dto;

import com.festago.auth.dto.command.AdminSignupCommand;
import jakarta.validation.constraints.NotBlank;

public record AdminSignupV1Request(
    @NotBlank
    String username,
    @NotBlank
    String password
) {

    public AdminSignupCommand toCommand() {
        return AdminSignupCommand.builder()
            .username(username)
            .password(password)
            .build();
    }
}
